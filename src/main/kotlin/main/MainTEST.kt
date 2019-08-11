package main

import game.MazeSolver
import constants.Constants.mazeOnlyOneRoad
import constants.Constants.playerStartX
import constants.Constants.playerStartY

class MainTEST {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val game = MazeSolver(mazeOnlyOneRoad, playerStartX, playerStartY)
            println("Path to exit is: ${game.playGameForOnlyOneRoad().map { "(${it.mazeXPosition},${it.mazeYPosition})" }}")

        }
    }
}