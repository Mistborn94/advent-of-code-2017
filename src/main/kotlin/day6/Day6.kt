package day6

import Util

fun main(args: Array<String>) {
    val fileFromClasspath = Util.getFileFromClasspath("Day6.txt")

    val input = fileFromClasspath.readLines().first().split("\\s".toRegex()).map { it.toInt() }

    val (resultA, resultB) = calculateIterations(input)
    println("A: $resultA")
    println("B: $resultB")
}


fun calculateIterations(input: List<Int>): Pair<Int, Int> {
    val bankSize = input.size
    val mutableInput = input.toMutableList()

    val seenConfigs = mutableMapOf<List<Int>, Int>()
    while (!seenConfigs.contains(mutableInput)) {
        seenConfigs.put(mutableInput.toList(), seenConfigs.size)

        var index = mutableInput.indices.maxBy { mutableInput[it] } ?: 0
        var redistributeCount = mutableInput[index]
        mutableInput[index] = 0

        while (redistributeCount > 0) {
            index = (index + 1) % bankSize
            mutableInput[index] += 1
            --redistributeCount
        }
    }

    return Pair(
            seenConfigs.size,
            seenConfigs.size - (seenConfigs[mutableInput] ?: 0)
    )
}
