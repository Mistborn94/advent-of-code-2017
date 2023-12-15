package day23

import FileUtil


fun main(args: Array<String>) {

    val text = FileUtil.getFileFromClasspath("Day23.txt").readText().trimEnd()

    val coProcessor = Duet()
    coProcessor.setInstructions(text.lines())
    println("A [8281]: ${coProcessor.evaluate()}")
    println("B [911]: ${solveB()}")
}

fun solveB(b: Int = 109300, c: Int = b + 17000, step: Int = 17): Int {

    val range = b..c step step
    return range.count { isNotPrime(it) }
}

fun isNotPrime(b: Int): Boolean {
    var found = false

    var d = 2
    do {
        var e = d
        do {
            if (d * e == b) {
                found = true
            }
            ++e
        } while (e <= b/d && !found)
        d++
    } while (d <= b/2 && !found)

    return found

}

