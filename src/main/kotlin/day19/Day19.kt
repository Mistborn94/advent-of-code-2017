package day19

import FileUtil

open class Point(val row: Int, val column: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        return row == other.row && column == other.column
    }

    override fun hashCode(): Int = 31 * row + column
}

class Grid(val grid: List<String>) {

    init {
        initMembers()
    }

    lateinit var currentDirection: Point
    lateinit var currentPosition: GridPoint
    lateinit var seenLetters: MutableList<Char>
    var stepCount: Int = 0

    inner class GridPoint(row: Int, column: Int) : Point(row, column) {

        val isValidIndex = row in grid.indices && column in grid[row].indices
        val value: Char get() = grid[row][column]
        val isBlank: Boolean get() = value.isWhitespace()

        private fun getNeighbours(previous: GridPoint): List<GridPoint> = listOf(
                GridPoint(row + 1, column),
                GridPoint(row - 1, column),
                GridPoint(row, column + 1),
                GridPoint(row, column - 1))
                .filter { it.isValidIndex && !it.isBlank }
                .filter { it != previous }

        fun getNext() {
            when (value) {
                in 'A'..'Z' -> {
                    seenLetters.add(value)
                    currentPosition = this + currentDirection
                }
                '+' -> {
                    val previous = this - currentDirection
                    val neighbours = getNeighbours(previous)
                    if (neighbours.size != 1) {
                        throw Exception("Invalid neighbour count $neighbours for cell [$row, $column]")
                    }
                    currentPosition = neighbours[0]
                    currentDirection = currentPosition - this

                }
                else -> {
                    currentPosition = this + currentDirection
                }
            }
        }

        operator fun plus(other: Point): GridPoint = GridPoint(this.row + other.row, this.column + other.column)

        operator fun minus(other: Point): GridPoint = GridPoint(this.row - other.row, this.column - other.column)
    }

    fun doLoop(): MutableList<Char> {
        initMembers()

        while (currentPosition.isValidIndex && !currentPosition.isBlank) {
            currentPosition.getNext()
            stepCount++
        }
        return seenLetters
    }

    private fun initMembers() {
        currentDirection = Point(+1, 0)
        currentPosition = GridPoint(0, grid[0].indexOf('|'))
        seenLetters = mutableListOf()
        stepCount = 0
    }

}

fun main(args: Array<String>) {
    val grid = Grid(FileUtil.getFileFromClasspath("Day19.txt").readLines())

    val seenLetters = grid.doLoop().joinToString("")
    println("A: $seenLetters")
    println("B: ${grid.stepCount}")
}