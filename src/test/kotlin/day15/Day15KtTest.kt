package day15

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15KtTest {

    private val initialA: Long = 65
    private val initialB: Long = 8921

    @Test
    fun countBasicMatching() {
        assertEquals(1, countPart1(initialA, initialB, 5).toLong())
        assertEquals(588L, countPart1(initialA, initialB, 40_000_000).toLong())
    }

    @Test
    fun countAdvancedMatching() {
        assertEquals(0, countPart2(initialA, initialB, 5).toLong())
        assertEquals(309, countPart2(initialA, initialB, 5_000_000).toLong())
    }
}