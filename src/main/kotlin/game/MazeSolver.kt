package game

import constants.Constants.COLUMN
import constants.Constants.ROW
import constants.Constants.WALL
import constants.Constants.playerEndGameAtCoordinateXRoud1
import constants.Constants.playerEndGameAtCoordinateXRoud2
import constants.Constants.playerEndGameAtCoordinateXSingleRound
import constants.Constants.playerEndGameAtCoordinateYRoud1
import constants.Constants.playerEndGameAtCoordinateYRoud2
import constants.Constants.playerEndGameAtCoordinateYSingleRound
import constants.Constants.playerMoveDirections
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

typealias SquareMaze = List<List<Int>>

class MazeSolver(
    private val maze: SquareMaze,
    private val playerStartPositionX: Int = 0,
    private val playerStartPositionY: Int = 0
) {

    fun playGameForOnlyOneRoad(): List<VisitedCoordinates> {
        //Add the player start position to the list of moves
        val listOfMoves = mutableListOf<VisitedCoordinates>()
        listOfMoves.addPlayerStartPositionCoordinates(playerStartPositionX, playerStartPositionY)
        //Set current position
        var currentPosition = listOfMoves[0]
        var hadFoundTheExit = false
        while (!hadFoundTheExit) {
            //I can't use foreach because I lose continue keyword
            //For current position I will check all directions to see if I have a valid move
            for (direction in playerMoveDirections) {
                //Here I don't care about the last argument that is the name of the thread. I will take this argument into
                //consideration only in function playGameForMultipleRoadsAndReturnTheShortestOne
                val nextMove = VisitedCoordinates(
                    currentPosition.mazeXPosition + direction[ROW],
                    currentPosition.mazeYPosition + direction[COLUMN],
                    Thread.currentThread().name
                )

                //Before continuing with any other validation. I need to check if the next position is not out of maze bounds
                if (!maze.isMoveValid(nextMove)) {
                    continue
                }

                if (maze.isWall(nextMove)) {
                    continue
                }

                if (maze.isMazeExitingPointForSingleRoad(nextMove)) {
                    listOfMoves.add(nextMove)
                    hadFoundTheExit = true
                    break
                }

                if (listOfMoves.isExplored(nextMove)) {
                    continue
                }

                listOfMoves.add(nextMove)
                currentPosition = nextMove
                //println("Current position: $currentPosition")
                break
            }
        }

        return listOfMoves
    }

    fun playGameForMultipleRoadsAndReturnTheShortestOne(): List<VisitedCoordinates> {
        //I am starting this game from the premise that every step the player makes has the same value
        //So at the end I will count the total number of steps that I had to make in order to reach the end
        val howManyTestToDo = 90
        // I will run every test on one thread. Also I will create a threads pool that has half threads of the tests
        // that I will run. "howManyTestToDo shr 1" is equel with "howManyTestToDo / 2"
        val executor = Executors.newFixedThreadPool((howManyTestToDo shr 1))
        //Create list to store all the threads
        val listOfFutureThreads = mutableListOf<Future<List<VisitedCoordinates>>>()
        for(currentTest in 0 until howManyTestToDo) {
            listOfFutureThreads.add(executor.submit<List<VisitedCoordinates>> { playGameForOnlyOneRoad() })
        }

        // Now because I can't cancel the JVM thread I will block it until all future tasks are completed.
        // This is not code for production and I know that this is very bad. So only this time I will break the rules

        while (listOfFutureThreads.all { it.isDone }) {
            TimeUnit.SECONDS.sleep(1L)
        }

        //Now I now, that all future tasks had finished. Now I need to store the best restuls
        var bestTime = Int.MAX_VALUE
        var bestPath = emptyList<VisitedCoordinates>()
        listOfFutureThreads.forEach { futureTask ->
            val pathToExit = futureTask.get()
            if(bestTime < pathToExit.size) {
                bestTime = pathToExit.size
                bestPath = pathToExit
            }
        }

        return bestPath
    }

}


fun List<List<Int>>.isWall(nextMove: VisitedCoordinates) =
    (WALL == this[nextMove.mazeXPosition][nextMove.mazeYPosition])

//Because the maze is a square the length and width are equal
fun List<List<Int>>.isMoveValid(nextMove: VisitedCoordinates) =
    !((nextMove.mazeXPosition < 0 || nextMove.mazeYPosition >= this[0].size)
            || (nextMove.mazeYPosition < 0 || nextMove.mazeYPosition >= this[0].size))

fun List<List<Int>>.isMazeExitingPointForSingleRoad(nextMove: VisitedCoordinates) =
    (nextMove.mazeXPosition == playerEndGameAtCoordinateXSingleRound && nextMove.mazeYPosition == playerEndGameAtCoordinateYSingleRound)

fun List<List<Int>>.isMazeExitingPointForMultipleRoads(nextMove: VisitedCoordinates) =
    (nextMove.mazeXPosition == playerEndGameAtCoordinateXRoud1 && nextMove.mazeYPosition == playerEndGameAtCoordinateYRoud1)
            || (nextMove.mazeXPosition == playerEndGameAtCoordinateXRoud2 && nextMove.mazeYPosition == playerEndGameAtCoordinateYRoud2)

fun MutableList<VisitedCoordinates>.isExplored(nextMove: VisitedCoordinates) =
    any { it.mazeXPosition == nextMove.mazeXPosition && it.mazeYPosition == nextMove.mazeYPosition }

fun MutableList<VisitedCoordinates>.addPlayerStartPositionCoordinates(row: Int, column: Int) =
    add(VisitedCoordinates(row, column, Thread.currentThread().name))

fun MutableList<VisitedCoordinates>.howManyStepsHadITookFromStartToStop() = this.size