package day16

import FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16KtTest {

    @Test
    fun dance_actualInput() {
        val steps = FileUtil.getFileFromClasspath("Day16.txt").readText().split(",")
        assertEquals("jcobhadfnmpkglie", dance(steps, 'a'..'p'))
        assertEquals("pclhmengojfdkaib", dance(steps, 'a'..'p', 1_000_000))
    }


    @Test
    fun dance_demoInput() {

        val demoSteps = "s1,x3/4,pe/b".split(",")
        val demoRange = 'a'..'e'

        assertEquals("baedc", dance(demoSteps, demoRange, 1))
        assertEquals("ceadb", dance(demoSteps, demoRange, 2))
        assertEquals("ecbda", dance(demoSteps, demoRange, 3))

        assertEquals("abcde", dance(demoSteps, demoRange, 4))
        assertEquals("ceadb", dance(demoSteps, demoRange, 6))
        assertEquals("baedc", dance(demoSteps, demoRange, 13))
    }
}