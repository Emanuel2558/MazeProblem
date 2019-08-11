package main

import constants.Constants.mazeOnlyMultipleRoads
import game.MazeSolver
import constants.Constants.mazeOnlyOneRoad
import constants.Constants.playerStartX
import constants.Constants.playerStartY

class MainTEST {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var game = MazeSolver(mazeOnlyOneRoad, playerStartX, playerStartY)
            //println("Path to exit is: ${game.playGameForOnlyOneRoad().map { "(${it.mazeXPosition},${it.mazeYPosition})" }}")
            game = MazeSolver(mazeOnlyMultipleRoads, playerStartX, playerStartY)
            println("Path to exit is: ${game.playGameForMultipleRoadsAndReturnTheShortestOne().map { "(${it.mazeXPosition},${it.mazeYPosition})" }}")

        }
    }
}