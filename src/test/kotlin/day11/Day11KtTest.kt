package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11KtTest {

    @Test
    internal fun getStepCount() {
        assertEquals(3, countSteps("ne,ne,ne".split(",")), "ne,ne,ne")
        assertEquals(0, countSteps("ne,ne,sw,sw".split(",")), "ne,ne,sw,sw")
        assertEquals(2, countSteps("ne,ne,s,s".split(",")), "ne,ne,s,s")
        assertEquals(3, countSteps("se,sw,se,sw,sw".split(",")), "se,sw,se,sw,sw")
        assertEquals(0, countSteps("ne,ne,s,s,nw,nw".split(",")), "ne,ne,s,s,nw,nw")
        assertEquals(2, countSteps("ne,ne,s,s,nw,nw,s,s".split(",")), "ne,ne,s,s,nw,nw,s,s")
    }
}