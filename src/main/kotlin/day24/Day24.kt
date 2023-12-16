package day24

import FileUtil
import java.util.*

data class Connector(val first: Int, val second: Int) {

    val score = first + second

    fun otherConnector(connect: Int): Int {
        return if (first == connect) second else first
    }

    override fun toString(): String {
        return "$first/$second"
    }
}

typealias CacheKey = Pair<Int, Set<Connector>>
class BridgeNode(
    val allConnectors: Map<Int, Set<Connector>>,
    val endConnector: Int,
    val seenConnectors: Set<Connector>
) {

    val cacheKey = endConnector to seenConnectors
    val score: Int = seenConnectors.sumOf { it.score }
    val length: Int = seenConnectors.size

    private val possibleNeighbours = allConnectors[endConnector] ?: emptySet()

    fun neighbours(): Iterable<BridgeNode> {
        val possible = possibleNeighbours.filter { it !in seenConnectors }
        return possible.map {
            BridgeNode(allConnectors, it.otherConnector(endConnector), seenConnectors + it)
        }
    }

}

fun solve(lines: List<String>): Pair<BridgeNode, BridgeNode> {
    val allNodes = mutableMapOf<Int, MutableSet<Connector>>()

    lines.forEach { line ->
        val split = line.split("/").map { it.toInt() }
        val connector = Connector(split[0], split[1])
        allNodes.getOrPut(connector.first) { mutableSetOf() }.add(connector)
        allNodes.getOrPut(connector.second) { mutableSetOf() }.add(connector)
    }

    val startingNode = BridgeNode(allNodes, 0, emptySet())

    return findStrongestPathBfs(startingNode)

}

fun findStrongestPathBfs(
    start: BridgeNode
): Pair<BridgeNode, BridgeNode> {
    val toVisit = PriorityQueue(Comparator<BridgeNode> { o1, o2 -> o2.score.compareTo(o1.score) })
    toVisit.add(start)

    val seenConfigurations = mutableSetOf<CacheKey>()

    var strongest = start
    var longest = start

    while (toVisit.isNotEmpty()) {
        val visit = toVisit.remove()
        if (visit.score > strongest.score) {
            strongest = visit
        }

        if ((visit.length > longest.length) || (visit.length == longest.length && visit.score > longest.score)) {
            longest = visit
        }

        if (seenConfigurations.add(visit.cacheKey)) {
            val neighbours = visit.neighbours().filter { it.cacheKey !in seenConfigurations }
            toVisit.addAll(neighbours)
        }
    }

    return strongest to longest
}

val sampleText = """
0/2
2/2
2/3
3/4
3/5
0/1
10/1
9/10
""".trimIndent()

fun main(args: Array<String>) {

    val text = FileUtil.getFileFromClasspath("Day24.txt").readText().trimEnd()

    val sample = solve(sampleText.lines())
    println("Sample 1A [31]: ${sample.first.score}")
    println("Sample 1B [19]: ${sample.second.score}")
    val solve = solve(text.lines())
    println("A [1940]: ${solve.first.score}")
    println("B [1928]: ${solve.second.score}")
}
