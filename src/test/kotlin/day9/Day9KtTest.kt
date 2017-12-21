package day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Day9KtTest {

    @ParameterizedTest
    @CsvSource(value = [
        "1, 0, '{}'",
        "3, 0, '{{}}'",
        "6, 0, '{{{}}}'",
        "10, 0, '{{{{}}}}'",
        "5, 0, '{{},{}}'",
        "16, 0, '{{{},{},{{}}}}'",
        "1, 4, '{<a>,<a>,<a>,<a>}'",
        "1, 1, '{<a!>>}'",
        "3, 2, '{<a!>>,{<a!>>}}'",
        "9, 8, '{{<ab>},{<ab>},{<ab>},{<ab>}}'",
        "9, 0, '{{<!!>},{<!!>},{<!!>},{<!!>}}'",
        "3, 17, '{{<a!>},{<a!>},{<a!>},{<ab>}}'",
        "1, 17, '{<random characters>}'",
        "1, 10, '{<{o\"i!a,<{i<a>}'"
    ])
    fun countGroups(expectedSum: Int, expectedGarbageSize: Int, input: String) {
        val sumGroup = sumGroup(input)
        assertEquals(expectedSum, sumGroup.score)
        assertEquals(expectedGarbageSize, sumGroup.garbageCount)
    }
}