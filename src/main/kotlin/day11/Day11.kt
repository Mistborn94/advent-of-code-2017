package day11

import FileUtil
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main(args: Array<String>) {
    val text = FileUtil.getFileFromClasspath("Day11.txt").readText()

    val steps = text.split(",")
    val results = (1..steps.size).map { countSteps(steps.subList(0, it)) }

    println("A: ${results.last()}")
    println("B: ${results.max()}")
}

fun countSteps(steps: List<String>): Int {
    val distanceCounts = steps.countGroups()
    val nDistance = distanceCounts.getOrDefault("n", 0) - distanceCounts.getOrDefault("s", 0)
    val nwDistance = distanceCounts.getOrDefault("nw", 0) - distanceCounts.getOrDefault("se", 0)
    val neDistance = distanceCounts.getOrDefault("ne", 0) - distanceCounts.getOrDefault("sw", 0)

    val eliminatedSteps = getEliminatedSteps(nDistance, neDistance, nwDistance)

    return nDistance.absoluteValue + nwDistance.absoluteValue + neDistance.absoluteValue - eliminatedSteps
}

fun <T> List<T>.countGroups() = this.groupingBy { it }.eachCount()

fun getEliminatedSteps(nDistance: Int, neDistance: Int, nwDistance: Int): Int {
    val eliminateFromAll = if (containsTriangle(nwDistance, neDistance, nDistance)) {
        minDistanceToZero(nDistance, nwDistance, neDistance) ?: 0
    } else 0

    return getBasicEliminatedSteps(nDistance.toZero(eliminateFromAll), neDistance.toZero(eliminateFromAll), nwDistance.toZero(eliminateFromAll)) + eliminateFromAll * 3
}

private fun containsTriangle(nwDistance: Int, neDistance: Int, nDistance: Int) =
        nwDistance.sign == neDistance.sign && nDistance.sign + nwDistance.sign == 0 && nDistance.sign != 0

private fun getBasicEliminatedSteps(nDistance: Int, neDistance: Int, nwDistance: Int): Int {
    return when {
        nwDistance.sign == neDistance.sign -> minDistanceToZero(nwDistance, neDistance)
        nDistance.sign + nwDistance.sign == 0 -> minDistanceToZero(nDistance, nwDistance)
        nDistance.sign + neDistance.sign == 0 -> minDistanceToZero(nDistance, neDistance)
        else -> 0
    }?.absoluteValue ?: 0
}

private fun minDistanceToZero(vararg values: Int): Int? {
    return values.minBy { it.absoluteValue }?.absoluteValue
}

fun Int.toZero(value: Int) = this - value * this.sign