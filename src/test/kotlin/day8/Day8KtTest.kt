package day8

import FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class Day8KtTest {

    @Test
    fun getMaxRegisterValue() {
        val file = FileUtil.getFileFromClasspath("Day8.txt")
        val cpu = Cpu(file)
        assertEquals(1, cpu.max)
        assertEquals(10, cpu.highestEver)
    }
}