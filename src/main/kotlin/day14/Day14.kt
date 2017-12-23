package day14

import day10.KnotHash
import java.util.*

fun main(args: Array<String>) {
    val input = "oundnydw"

    println("A: ${countPositives(input)}")
    println("B: ${countRegions(input)}")
}

val BITS = 128

fun countPositives(input: String): Int {
    return getBitSets(input).totalCardinality()
}

fun countRegions(intput: String): Int {
    val bitsets = getBitSets(intput)

    return countRegions(bitsets)
}

fun countRegions(bitsets: List<BitSet>): Int {
    var regions = 0
    while (bitsets.totalCardinality() > 0) {
        regions++
        val position = bitsets.getFirstSetBit()
        bitsets.visit(position)
    }

    return regions
}

fun Pair<Int, Int>.getNeighbours() = listOf(
        Pair(first - 1, second),
        Pair(first + 1, second),
        Pair(first, second + 1),
        Pair(first, second - 1)
)

private fun List<BitSet>.visit(position: Pair<Int, Int>) {
    val neighbours = position.getNeighbours().filter {
        it.first >= 0 && it.second >= 0 && it.first < this.size && it.second < this[0].size()
    }
    if (get(position)) {
        this.clear(position)
        neighbours.forEach {
            if (this.get(it)) {
                this.visit(it)
            }
        }
    }
}

fun List<BitSet>.totalCardinality(): Int = map(BitSet::cardinality).sum()

fun List<BitSet>.get(position: Pair<Int, Int>): Boolean = this[position.first].get(position.second)

fun List<BitSet>.clear(position: Pair<Int, Int>) = this[position.first].clear(position.second)

fun List<BitSet>.getFirstSetBit(): Pair<Int, Int> {
    val row = indexOfFirst { it.cardinality() > 0 }
    if (row == -1) {
        return Pair(-1, -1)
    }
    val col = this[row].nextSetBit(0)
    return Pair(row, col)
}

fun String.toUnsignedLong(radix: Int = 10) = java.lang.Long.parseUnsignedLong(this, radix)
fun String.hexStringToBitSet() = BitSet.valueOf(this.chunked(16).map { it.toUnsignedLong(16) }.reversed().toLongArray())!!

fun getBitSets(input: String): List<BitSet> {
    return (0 until BITS).map {
        KnotHash("$input-$it").hexHash.hexStringToBitSet()
    }
}

fun List<Int>.toLongArray(): LongArray = this.map { it.toLong() }.toLongArray()
