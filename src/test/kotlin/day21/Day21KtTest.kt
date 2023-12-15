package day21

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21KtTest {

    val sample1Input = """
            |../.# => ##./#../...
            |.#./..#/### => #..#/..../..../#..#"""
        .trimMargin()
    @Test
    fun sample1() {
        assertEquals(12, solveA(sample1Input, 2).pointCount)
    }

    @Test
    fun sample1A() {
        val output = """#..#
....
....
#..#"""
        assertEquals(output, solveA(sample1Input, 1).toString())
    }

    @Test
    fun sample1B() {
        val output = """##.##.
#..#..
......
##.##.
#..#..
......"""
        assertEquals(output, solveA(sample1Input, 2).toString())
    }
}