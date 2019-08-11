package constants

object Constants {

    //region Maze common elements

    //region components only for one road
    /* Maze only one road:
      1 1 1 1 1
      1 1 1 0 0
      1 1 1 0 1
      0 0 0 0 1
      1 1 1 1 1
    */
    //beginning the road design
    val mazeOnlyOneRoad = listOf(
        listOf(1, 1, 1, 1, 1), listOf(1, 1, 1, 0, 0),
        listOf(1, 1, 1, 0, 1), listOf(0, 0, 0, 0, 1), listOf(1, 1, 1, 1, 1)
    )

    const val playerEndGameAtCoordinateXSingleRound = 1
    const val playerEndGameAtCoordinateYSingleRound = 4
    //endregion

    //region components for multiple roads
    /* Maze for multiple roads:
      1 1 1 1 1
      1 0 0 0 0
      1 0 1 0 1
      0 0 0 0 1
      1 1 1 0 0
    */

    //beginning the road design
    val mazeOnlyMultipleRoads = listOf(
        listOf(1, 1, 1, 1, 1), listOf(1, 0, 0, 0, 0),
        listOf(1, 0, 1, 0, 1), listOf(0, 0, 0, 0, 1), listOf(1, 1, 1, 0, 0)
    )

    const val playerEndGameAtCoordinateXRoud1 = 1
    const val playerEndGameAtCoordinateYRoud1 = 4
    const val playerEndGameAtCoordinateXRoud2 = 4
    const val playerEndGameAtCoordinateYRoud2 = 4

    //endregion

    const val playerStartX = 3
    const val playerStartY = 0

    const val ROW = 0
    const val COLUMN = 1

    //const val ROAD = 0
    const val WALL = 1

    val playerMoveDirections = listOf(listOf(0, 1), listOf(1, 0), listOf(0, -1), listOf(-1, 0))
    //endregion
}