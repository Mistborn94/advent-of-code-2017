package day17

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17KtTest {

    @Test
    fun countUpTo() {
        assertEquals(listOf(0, 9, 5, 7, 2, 4, 3, 8, 6, 1), countUpTo(9, 3))
    }

    @Test
    internal fun afterZero() {
        assertEquals(5, afterZero(8, 3))
        assertEquals(9, afterZero(9, 3))
    }
}