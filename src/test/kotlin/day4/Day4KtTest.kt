package day4

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Day4KtTest {

    @Test
    fun isValidUnique() {
        assertTrue(isValidUnique("aa bb cc dd ee"))
        assertFalse(isValidUnique("aa bb cc dd aa"))
        assertTrue(isValidUnique("aa bb cc dd aaa"))
    }

    @Test
    fun isValidAnagram() {
        assertTrue(isValidAnagram("abcde fghij"))
        assertFalse(isValidAnagram("abcde xyz ecdab"))
        assertTrue(isValidAnagram("a ab abc abd abf abj"))
        assertTrue(isValidAnagram("iiii oiii ooii oooi oooo"))
        assertFalse(isValidAnagram("oiii ioii iioi iiio"))
    }
}