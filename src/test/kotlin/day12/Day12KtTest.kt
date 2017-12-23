package day12

import FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12KtTest {
    @Test
    internal fun countGroupSize() {
        val file = FileUtil.getFileFromClasspath("Day12.txt")

        val graph = file.useLines { buildGraph(it) }
        assertEquals(6, getGroup(graph, 0).size)
        assertEquals(1, getGroup(graph, 1).size)
        assertEquals(2, countGroups(graph))
    }
}

