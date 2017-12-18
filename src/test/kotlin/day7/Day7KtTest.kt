package day7

import Util
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7KtTest {

    @Test
    fun buildTree() {
        val file = Util.getFileFromClasspath("Day7/sample.txt")
        val rootNode = buildTree(file)

        assertEquals("tknk", rootNode.name)

        val (node, difference) = rootNode.findUnbalancedNode()
        assertEquals("ugml", node!!.name)
        assertEquals(-8, difference)
    }
}