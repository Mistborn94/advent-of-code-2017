package day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3KtTest {

    @Test
    fun findSteps() {
        assertEquals(1, findSteps(8))
        assertEquals(3, findSteps(12))
        assertEquals(4, findSteps(13))
        assertEquals(2, findSteps(23))
        assertEquals(31, findSteps(1024))
    }

    @Test
    fun findNextSum() {
        assertEquals(2, findNextSum(1))
        assertEquals(4, findNextSum(2))
        assertEquals(11, findNextSum(10))
        assertEquals(133, findNextSum(125))
        assertEquals(747, findNextSum(700))
        assertEquals(362, findNextSum(352))
        assertEquals(362, findNextSum(361))
        assertEquals(747, findNextSum(362))
    }
}