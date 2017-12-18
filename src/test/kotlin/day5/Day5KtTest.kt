package day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5KtTest {

    private val MAZE = arrayOf(0, 3, 0, 1, -3)

    @Test
    internal fun getBasicStepCount() {
        assertEquals(5, getBasicStepCount(MAZE))
    }

    @Test
    internal fun getAlternateStepCount() {
        assertEquals(10, getAlternateStepCount(MAZE))
    }
}