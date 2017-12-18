package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6KtTest {

    @Test
    fun calculateIterations() {
        val (resultA, resultB) = calculateIterations(listOf(0, 2, 7, 0))
        assertEquals(5, resultA)
        assertEquals(4, resultB)
    }
}