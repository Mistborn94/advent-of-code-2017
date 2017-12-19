package day7

import FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7KtTest {

    @Test
    fun buildTree() {
        val file = FileUtil.getFileFromClasspath("Day7/sample.txt")
        val rootNode = buildTree(file)

        assertEquals("tknk", rootNode.name)

        val (node, difference) = rootNode.unbalancedNode
        assertEquals("ugml", node!!.name)
        assertEquals(-8, difference)
    }
}