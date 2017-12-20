package day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Day9KtTest {

    @ParameterizedTest
    @CsvSource(value = [
        "1, '{}'",
        "3, '{{}}'",
        "6, '{{{}}}'",
        "10, '{{{{}}}}'",
        "5, '{{},{}}'",
        "16, '{{{},{},{{}}}}'",
        "1, '{<a>,<a>,<a>,<a>}'",
        "1, '{<a!>>}'",
        "3, '{<a!>>,{<a!>>}}'",
        "9, '{{<ab>},{<ab>},{<ab>},{<ab>}}'",
        "9, '{{<!!>},{<!!>},{<!!>},{<!!>}}'",
        "3, '{{<a!>},{<a!>},{<a!>},{<ab>}}'"
    ])
    fun countGroups(expected: Int, input: String) {
        assertEquals(expected, sumAllGroups(input))
    }
}