package day2

import Util
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2KtTest {

    @Test
    fun getLargeSmallChecksum() {
        val file = Util.getFileFromClasspath("Day2/sample.txt")
        assertEquals(18, getLargeSmallChecksum(file))
    }

    @Test
    fun getDivisionChecksum() {
        val file = Util.getFileFromClasspath("Day2/sample2.txt")
        assertEquals(9, getDivisionChecksum(file))
    }
}