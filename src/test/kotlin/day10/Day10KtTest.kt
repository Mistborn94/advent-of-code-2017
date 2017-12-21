package day10

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class Day10KtTest {

    companion object {

        @JvmStatic
        fun createInputsA(): Stream<Arguments> {
            return sequenceOf(
                    Arguments.of(listOf(3), listOf(2, 1, 0, 3, 4)),
                    Arguments.of(listOf(3, 4), listOf(4, 3, 0, 1, 2)),
                    Arguments.of(listOf(3, 4, 1), listOf(4, 3, 0, 1, 2)),
                    Arguments.of(listOf(3, 4, 1, 5), listOf(3, 4, 2, 1, 0))
            ).asStream()
        }

        @JvmStatic
        fun createInputsB1(): Stream<Arguments> {
            return sequenceOf(
                    Arguments.of("", "a2582a3a0e66e6e86e3812dcb672a272"),
                    Arguments.of("AoC 2017", "33efeb34ea91902bb2f59c9920caa6cd"),
                    Arguments.of("1,2,3", "3efbe78a8d82f29979031a4aa0b16a9d"),
                    Arguments.of("1,2,4", "63960835bcdc130f0b66d7ff4f6a5a8e")
            ).asStream()
        }
    }

    @ParameterizedTest
    @MethodSource("createInputsA")
    fun testApplyHashA(lengths: List<Int>, expected: List<Int>) {
        val knotHash = KnotHash(lengths, 5)
        assertEquals(expected, knotHash.finalList)
    }

    @ParameterizedTest
    @MethodSource("createInputsB1")
    fun testApplyHashB1(input: String, expected: String) {
        val result = getAsciiDenseHash(input)
        assertEquals(expected, result)
    }

    @Test
    fun testBuildLengthsList() {
        assertEquals(listOf(49, 44, 50, 44, 51, 17, 31, 73, 47, 23), buildLengthsList("1,2,3"))
        assertEquals(listOf(49, 44, 50, 44, 51, 17, 31, 73, 47, 23), buildLengthsList("1,2,3"))
    }

    @Test
    fun testToHexString() {
        val input = listOf(64, 7, 255)
        assertEquals("4007ff", input.toHexString())
    }

    @Test
    fun testXorReduce() {
        val input = listOf(65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22)
        assertEquals(64, input.xor())


        val longInput = input + input
        assertEquals(listOf(64, 64), longInput.chunked(16).map(List<Int>::xor))
    }
}