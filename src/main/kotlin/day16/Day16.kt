package day16

import FileUtil

fun main(args: Array<String>) {

    val steps = FileUtil.getFileFromClasspath("Day16.txt").readText().split(",")
    val charRange = 'a'..'p'

    println("A ${dance(steps, charRange)}")
    println("B ${dance(steps, charRange, 1_000_000_000)}")
}

fun dance(stepStrings: List<String>, programRange: CharRange, iterations: Int = 1): String {
    val programs = programRange.toMutableList()
    val steps = stepStrings.map { DanceMove.getMove(it) }
    val cache = mutableMapOf<String, Pair<String, Int>>()

    var previous = ""
    var last = programs.toDanceString()

    (0 until iterations).asSequence().takeWhile { !cache.contains(last) }.forEach {
        programs.applyDanceMoves(steps)

        previous = last
        last = programs.toDanceString()

        cache[previous] = last to it
    }

    if (cache.size < iterations) {
        val cycleSize = cache[previous]!!.second - cache[last]!!.second + 1
        val remainder = iterations % cycleSize
        (0 until remainder).forEach {
            previous = last
            last = cache[previous]!!.first
        }
    }

    return last
}

private fun MutableList<Char>.applyDanceMoves(simpleSteps: List<DanceMove>) {
    simpleSteps.forEach { it.apply(this) }
}

private fun MutableList<Char>.toDanceString() = joinToString("")