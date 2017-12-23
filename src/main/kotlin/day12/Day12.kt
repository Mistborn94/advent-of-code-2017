package day12

fun main(args: Array<String>) {
    val file = FileUtil.getFileFromClasspath("Day12.txt")

    val graph = file.useLines { buildGraph(it) }
    val countGroupSize = getGroup(graph, 0).size

    println("A: $countGroupSize")
    println("A: ${countGroups(graph)}")
}

fun countGroups(graph: Map<Int, Collection<Int>>): Int {
    val visited = mutableSetOf<Int>()

    var groupCount = 0
    while (visited.size < graph.size) {
        val node = graph.keys.first { !visited.contains(it) }
        visited.addAll(getGroup(graph, node))
        groupCount++
    }
    return groupCount;
}

fun getGroup(graph: Map<Int, Collection<Int>>, i: Int): Collection<Int> {
    val visited = mutableSetOf<Int>()
    val toVisit = mutableSetOf(i)

    while (!toVisit.isEmpty()) {
        val node = toVisit.first()
        toVisit.remove(node)
        visited.add(node)
        val newVisits = graph[node]!!
        toVisit.addAll(newVisits.filter { !visited.contains(it) })
    }
    return visited
}

val connectedPattern = """ (\d+)(?:,|$)""".toRegex()
fun buildGraph(lines: Sequence<String>): MutableMap<Int, Collection<Int>> {

    val groups: MutableMap<Int, Collection<Int>> = mutableMapOf()

    lines.forEach {
        val index = it.substringBefore(' ').toInt()
        val connected = connectedPattern.findAll(it).map {
            it.destructured.component1().toInt()
        }.toList()
        groups[index] = connected
    }
    return groups
}

