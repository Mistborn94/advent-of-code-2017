package day2

import FileUtil
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val input = FileUtil.getFileFromClasspath("Day2.txt")

    println(input.absolutePath)
    println("A: ${getLargeSmallChecksum(input)}")
    println("B: ${getDivisionChecksum(input)}")
}

fun getLargeSmallChecksum(inputFile: File): Int {
    var sum = 0
    inputFile
            .forEachLine {
                val numberList = numberList(it)

                sum += (numberList.max()!! - numberList.min()!!)
            }
    return sum
}

private fun numberList(line: String): List<Int> {
    return line
            .split("\\s".toRegex())
            .map { it.toInt() }
}

fun getDivisionChecksum(inputFile: File): Int {
    var sum = 0
    inputFile
            .forEachLine {
                val numberList = numberList(it)
                sum += getDivisionResult(numberList)
            }
    return sum
}

private fun getDivisionResult(numberList: List<Int>): Int {
    for (first in IntRange(0, numberList.size - 2)) {
        for (second in IntRange(first + 1, numberList.size - 1)) {
            val firstValue = numberList[first]
            val secondValue = numberList[second]

            val maxValue = max(firstValue, secondValue)
            val minValue = min(firstValue, secondValue)

            if (maxValue % minValue == 0) {
                return maxValue / minValue
            }
        }
    }
    return 0
}