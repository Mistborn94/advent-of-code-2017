package day22

import FileUtil
import helper.point.Direction
import helper.point.Point
import helper.point.findAll

fun solveA(text: String, bursts: Int = 10000): Int {
    val inputLines = text.lines()

    val cells = inputLines.findAll('#').toMutableSet()
    var direction = Direction.UP
    var position = Point(inputLines[0].length / 2, inputLines.size / 2)
    var infectionBursts = 0

    repeat(bursts) {
        val infected = position in cells
        if (infected) {
            cells.remove(position)
            direction = direction.right
        } else {
            cells.add(position)
            direction = direction.left
            infectionBursts++
        }
        position += direction.pointPositiveDown
    }

    return infectionBursts
}

fun solveB(text: String, bursts: Int = 10000000): Int {
    val inputLines = text.lines()

    val infectedCells = inputLines.findAll('#').toMutableSet()
    val weakenedCells = mutableSetOf<Point>()
    val flaggedCells = mutableSetOf<Point>()
    var direction = Direction.UP
    var position = Point(inputLines[0].length / 2, inputLines.size / 2)
    var infectionBursts = 0

    repeat(bursts) {
         when (position) {
             in flaggedCells -> {
                 flaggedCells.remove(position)
                 direction = direction.opposite
             }
             in infectedCells -> {
                 infectedCells.remove(position)
                 flaggedCells.add(position)
                 direction = direction.right
             }
             in weakenedCells -> {
                 weakenedCells.remove(position)
                 infectedCells.add(position)
                 infectionBursts++
             }
             else -> {
                 weakenedCells.add(position)
                 direction = direction.left
             }
         }
        position += direction.pointPositiveDown
    }

    return infectionBursts
}

val sampleText = """..#
#..
..."""

fun main(args: Array<String>) {

    val text = FileUtil.getFileFromClasspath("Day22.txt").readText().trimEnd()

    println("Sample A 1 bursts [1]: ${solveA(sampleText, 1)}")
    println("Sample A 7 bursts [5]: ${solveA(sampleText, 7)}")
    println("Sample A 70 bursts [41]: ${solveA(sampleText, 70)}")
    println("Sample A [5587]: ${solveA(sampleText)}")
    println("A [5565]: ${solveA(text)}")

    println("Sample B 100 bursts - [26]: ${solveB(sampleText, 100)}")
    println("Sample B 10000000 bursts - [2511944]: ${solveB(sampleText)}")
    println("B: ${solveB(text,)}")
}