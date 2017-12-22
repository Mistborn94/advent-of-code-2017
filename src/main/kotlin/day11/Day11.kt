package day11

import FileUtil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main(args: Array<String>) {
    val file = FileUtil.getFileFromClasspath("Day9.txt")
//    val steps = file.readText().split(",").groupingBy { it }.eachCount()
    val steps = "ne,ne,ne".split(",").groupingBy { it }.eachCount()

    println(steps)
    var nDistance = steps.getOrDefault("n", 0) - steps.getOrDefault("s", 0)
    var nwDistance = steps.getOrDefault("nw", 0) - steps.getOrDefault("se", 0)
    var neDistance = steps.getOrDefault("ne", 0) - steps.getOrDefault("sw", 0)
    println("${nDistance}, ${nwDistance}, ${neDistance}")
    /* NW, NE -> N
    *   1,  1 -> 1
    *  -1, -1 -> -1
    * */

    /**
     * NW: -2: 0
     * NE: -4: -2
     *
     * vertical -2
     */
    when {
        nwDistance.sign == neDistance.sign -> {

            val eliminatedSteps = closestToZeroSameSign(nwDistance, neDistance)
            println("Eliminating E/W: $eliminatedSteps")
            nwDistance -= eliminatedSteps
            neDistance -= eliminatedSteps
            nDistance += eliminatedSteps
        }
        nDistance.sign == neDistance.sign -> {
            val eliminatedSteps = closestToZeroSameSign(nDistance, nwDistance)
            println("Eliminating N/W: $eliminatedSteps")
            nDistance -= eliminatedSteps
            nwDistance -= eliminatedSteps
            neDistance += eliminatedSteps
        }
        else -> {
            val eliminatedSteps = closestToZeroSameSign(nDistance, neDistance)
            println("Eliminating N/E: $eliminatedSteps")
            nDistance -= eliminatedSteps
            neDistance -= eliminatedSteps
            nwDistance += eliminatedSteps
        }
    }
    print(nDistance + nwDistance + neDistance)
}

private fun closestToZeroSameSign(dist1: Int, dist2: Int): Int {
    return if (dist1 == 0 || dist2 == 0) {
        0
    } else if (dist1 > 0) {
        min(dist1, dist2)
    } else {
        max(dist1, dist2)
    }
}