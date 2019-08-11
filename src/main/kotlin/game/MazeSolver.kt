package game

import constants.Constants.COLUMN
import constants.Constants.ROW
import constants.Constants.WALL
import constants.Constants.playerEndGameAtCoordinateXSingleRound
import constants.Constants.playerEndGameAtCoordinateYSingleRound
import constants.Constants.playerMoveDirections

typealias SquareMaze = List<List<Int>>

class MazeSolver(
    private val maze: SquareMaze,
    private val playerStartPositionX: Int = 0,
    private val playerStartPositionY: Int = 0
) {

    private val listOfMoves = mutableListOf<VisitedCoordinates>()
    private lateinit var currentPosition: VisitedCoordinates

    fun playGame(): List<VisitedCoordinates> {
        //Add the player start position to the list of moves
        listOfMoves.addPlayerStartPositionCoordinates(playerStartPositionX, playerStartPositionY)
        //Set current position
        currentPosition = listOfMoves[0]
        var hadFoundTheExit = false
        while (!hadFoundTheExit) {
            //I can't use foreach because I lose continue keyword
            //For current position I will check all directions to see if I have a valid move
            for (direction in playerMoveDirections) {
                val nextMove = VisitedCoordinates(
                    currentPosition.mazeXPosition + direction[ROW],
                    currentPosition.mazeYPosition + direction[COLUMN]
                )

                //Before continuing with any other validation. I need to check if the next position is not out of maze bounds
                if (!maze.isMoveValid(nextMove)) {
                    continue
                }

                if (maze.isWall(nextMove)) {
                    continue
                }

                if (maze.isMazeExistPoint(nextMove)) {
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

}


fun List<List<Int>>.isWall(nextMove: VisitedCoordinates) =
    (WALL == this[nextMove.mazeXPosition][nextMove.mazeYPosition])

//Because the maze is a square the length and width are equal
fun List<List<Int>>.isMoveValid(nextMove: VisitedCoordinates) =
    !((nextMove.mazeXPosition < 0 || nextMove.mazeYPosition >= this[0].size)
            || (nextMove.mazeYPosition < 0 || nextMove.mazeYPosition >= this[0].size))

fun List<List<Int>>.isMazeExistPoint(nextMove: VisitedCoordinates) =
    (nextMove.mazeXPosition == playerEndGameAtCoordinateXSingleRound && nextMove.mazeYPosition == playerEndGameAtCoordinateYSingleRound)

fun MutableList<VisitedCoordinates>.isExplored(nextMove: VisitedCoordinates) =
    any { it.mazeXPosition == nextMove.mazeXPosition && it.mazeYPosition == nextMove.mazeYPosition }

fun MutableList<VisitedCoordinates>.addPlayerStartPositionCoordinates(row: Int, column: Int) =
    add(VisitedCoordinates(row, column))