package day13

val pattern = """(\d+): (\d+)""".toRegex()
fun main(args: Array<String>) {
    val lines = FileUtil.getFileFromClasspath("Day13.txt").readLines()

    val firewall = buildFirewall(lines)
    println("A: ${calcSeverity(firewall)}")
    println("B: ${findDelay(firewall)}")
}

fun <K : Comparable<K>, V> Map<K, V>.maxKey(): K? = this.keys.max()

private fun findDelay(firewall: Map<Int, Int>): Int {
    return generateSequence(0, { it + 1 }).first { !isCaught(firewall, it) }
}

private fun isCaught(firewall: Map<Int, Int>, delay: Int = 0): Boolean =
        (0..firewall.maxKey()!!).firstOrNull { caughtAtLayer(firewall, it, delay) } != null

private fun caughtAtLayer(firewall: Map<Int, Int>, layer: Int, delay: Int = 0): Boolean {
    val range = firewall[layer]
    val time = layer + delay
    return when {
        range == null -> false
        time % (2 * range - 2) == 0 -> true
        else -> false
    }
}

private fun calcSeverity(firewall: Map<Int, Int>): Int {
    val max = firewall.maxKey()!!

    return (0..max).map {
        if (caughtAtLayer(firewall, it)) {
            it * firewall[it]!!
        } else {
            0
        }
    }.sum()
}

private fun buildFirewall(lines: List<String>): Map<Int, Int> = lines.map {
    pattern.matchEntire(it)!!.destructured
}.associate { (depth, range) -> Pair(depth.toInt(), range.toInt()) }
