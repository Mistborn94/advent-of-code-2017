package day21

import FileUtil
import cartesianProduct
import helper.point.Point
import helper.point.findAll

val startPattern = ".#.\n..#\n###".lines()
val startGrid = Grid(3, startPattern.findAll('#'))

data class Grid(val size: Int, val points: Set<Point>) {

    val chunkSize = if (size % 2 == 0) 2 else 3
    val pointCount: Int
        get() = points.size

    fun chunks(): Map<Point, Grid> {

        val chunksPerSide = size / chunkSize
        val gridMap = (0..<chunksPerSide).cartesianProduct(0..<chunksPerSide) { x, y -> Point(x, y)}
            .associateWith { mutableSetOf<Point>() }

        points.forEach {(x, y) ->
            val xChunk = x / chunkSize;
            val yChunk = y / chunkSize;

            val xOffset = xChunk * chunkSize
            val yOffset = yChunk * chunkSize

            gridMap[Point(xChunk, yChunk)]!!.add(Point(x - xOffset, y - yOffset))
        }

        return gridMap.mapValues { (_, value) -> Grid(chunkSize, value) }
    }

    override fun toString(): String {
        return toString("\n")
    }

    fun toString(lineSeparator: String = "\n"): String {
        return (0..<size).joinToString(separator = lineSeparator) { y ->
            (0..<size).joinToString(separator = "") { x ->
                if (Point(x, y) in points) "#" else "."
            }

        }
    }

    fun flipVertical(): Grid {
        val points = points.mapTo(mutableSetOf()) { (x, y) -> Point(size - x - 1, y) }
        val new = Grid(size, points)
        return new
    }

    fun flipHorizontal(): Grid {
        val squareSize = size
        val points = points.mapTo(mutableSetOf()) { (x, y) -> Point(x, squareSize - y - 1) }
        return Grid(squareSize, points)
    }

    fun rotate90(): Grid {
        val squareSize = size
        val points = points.mapTo(mutableSetOf()) { (x, y) -> Point(squareSize - 1 - y, x) }
        return Grid(squareSize, points)
    }

}

fun solveA(text: String, repetitions: Int = 5): Grid {
    val inputRules = text.lines().associate {
        val (a, b) = it.split(" => ")
        val key = a.split("/")
        val value = b.split("/")
        Grid(key.size, key.findAll('#')) to Grid(value.size, value.findAll('#'))
    }

    val sets = inputRules.entries.mapIndexed { i, (key, value) ->
        val rotate90 = key.rotate90()
        val rotate180 = rotate90.rotate90()
        val rotate270 = rotate180.rotate90()
        val set = setOf(
            key, key.flipVertical(), key.flipHorizontal(),
            rotate90, rotate180, rotate270,
            rotate90.flipHorizontal(), rotate90.flipVertical(),
            rotate180.flipHorizontal(), rotate180.flipVertical(),
            rotate270.flipHorizontal(), rotate270.flipVertical(),
        )
//        println("Original:")
//        println(key)
//        println("Transformations:")
//        val separator = "-".repeat(key.size)
//        println(set.drop(1).joinToString(separator = "\n$separator\n"))

        set to value
    }


    val allRules = sets.flatMap { (keys, value) -> keys.map { it to value } }
        .toMap()
    println("Sets - total key count = ${sets.sumOf { (key, _) -> key.size }}")
    println("Map - total key count = ${allRules.size}")

    var grid = startGrid
    repeat(repetitions) {

//        println("\nRepetition ${it + 1}")
//        println(grid)
//
//        println("Grid Chunks:")
//        println(grid.chunkedString())
        val chunks = grid.chunks()

        val newSideLength = (grid.size / grid.chunkSize) * (grid.chunkSize + 1)
        val newPoints = chunks.flatMapTo(mutableSetOf()) { (chunkId, chunk) ->
            val matched = allRules[chunk]!!
            val rulePoints = matched.points

//            println("Rule matched: ${chunk.toString(lineSeparator = "/")} => ${matched.toString(lineSeparator = "/")}")

            val newSize = chunk.size + 1
            val xOffset = chunkId.x * newSize
            val yOffset = chunkId.y * newSize
            rulePoints.map { (x, y) -> Point(x + xOffset, y + yOffset) }
        }
        grid = Grid(newSideLength, newPoints)
    }

//    println("\nFinal Repetition")
//    println(grid)

    return grid
}


private fun Map<Grid, Grid>.flipVertical(): Map<Grid, Grid> = mapKeys { (keyGrid, _) -> keyGrid.flipVertical() }
private fun Map<Grid, Grid>.flipHorizontal() = mapKeys { (keyGrid, _) -> keyGrid.flipHorizontal() }
private fun Map<Grid, Grid>.rotateKeys() = mapKeys { (key, _) -> key.rotate90() }


fun main(args: Array<String>) {
    val text = FileUtil.getFileFromClasspath("Day21.txt").readText().trimEnd()

    println("A: ${solveA(text).pointCount}")
    println("B: ${solveA(text, 18).pointCount}")
}