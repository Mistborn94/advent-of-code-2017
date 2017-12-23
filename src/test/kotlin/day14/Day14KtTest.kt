package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class Day14KtTest {

    @Test
    fun countPositives() {
        assertEquals(8108, countPositives(Companion.sampleInput))
        assertEquals(8, basicBits1.totalCardinality())
    }

    @Test
    fun countRegions() {
        BitSet.valueOf(byteArrayOf(0))

        assertEquals(2, countRegions(basicBits1))
        assertEquals(3, countRegions(basicBits2))
        assertEquals(2, countRegions(basicBits3))
        assertEquals(10, countRegions(basicBits4))
        assertEquals(3, countRegions(hexBits1))
        assertEquals(3, countRegions(hexBits2))
        assertEquals(3, countRegions(hexBits3))
        assertEquals(1242, countRegions(Companion.sampleInput))
    }

    companion object {
        private val basicBits1 get() = listOf<Byte>(0b0101, 0b0101, 0b0101, 0b0101).map { BitSet.valueOf(byteArrayOf(it)) }
        private val basicBits2 get() = listOf<Byte>(0b0101, 0b0011, 0b0101, 0b0101).map { BitSet.valueOf(byteArrayOf(it)) }
        private val hexBits1 get() = listOf("f", "8", "5", "0").map { it.hexStringToBitSet() }
        private val hexBits2 get() = listOf("ff", "81", "5C", "00").map { it.hexStringToBitSet() }
        private val hexBits3
            get() = listOf("f".repeat(32),
                    "0".repeat(32),
                    "f".repeat(15) + "00" + "f".repeat(15)
            ).map { it.hexStringToBitSet() }
        private val basicBits3 get() = listOf<Byte>(0b11111, 0b10001, 0b10101, 0b10001, 0b11101).map { BitSet.valueOf(byteArrayOf(it)) }
        private val basicBits4 get() = listOf<Long>(0b11010100, 0b01010101, 0b00001010, 0b10101101, 0b01101000, 0b11001001).map { BitSet.valueOf(longArrayOf(it)) }
        private val sampleInput = "flqrgnkx"
    }
}