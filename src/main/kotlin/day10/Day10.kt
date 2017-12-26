package day10

import normalizeIndex


fun main(args: Array<String>) {
    val input = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118"
    val result = getBasicHash(input.split(',').map(String::toInt))
    println("A: ${result[0] * result[1]}")

    val denseHash = getAsciiDenseHash(input)
    println("B $denseHash")
}

private fun getBasicHash(lengths: List<Int>): List<Int> {
    return KnotHash(lengths).finalList
}

fun getAsciiDenseHash(charSequence: CharSequence): String {
    return KnotHash(charSequence).hexHash
}

class KnotHash(lengths: List<Int>, repeat: Int = 64, private val listSize: Int = 256) {
    val finalList: List<Int>

    init {
        var currentList: List<Int> = (0 until listSize).toList()
        var currentSkipSize = 0
        var currentPosition = 0

        repeat(repeat, {

            lengths.forEach { length ->
                if (length <= listSize) {
                    currentList = currentList.reverseSection(currentPosition, length)
                    currentPosition = currentList.normalizeIndex(currentPosition + length + currentSkipSize)
                    currentSkipSize++
                }
            }
        })

        finalList = currentList.toList()
    }

    val hexHash = finalList
            .chunked(16)
            .map(List<Int>::xor).toHexString()

    constructor(string: CharSequence) : this(string.map(Char::toInt) + STANDARD_SUFFIX)

    companion object {
        val STANDARD_SUFFIX = listOf(17, 31, 73, 47, 23)
    }
}

fun <T> List<T>.shiftLeft(amount: Int): List<T> = this.subList(amount, size) + this.subList(0, amount)

fun <T> List<T>.shiftRight(amount: Int): List<T> = this.shiftLeft(size - amount)

fun <T> List<T>.reverseSection(start: Int, length: Int): List<T> {
    val shiftLeft = this.shiftLeft(start)
    val withReversed = shiftLeft.subList(0, length).reversed() + shiftLeft.subList(length, size)
    return withReversed.shiftRight(start)
}

fun List<Int>.xor(): Int = this.reduce { acc, i -> acc.xor(i) }

fun List<Int>.toHexString(): String = this.map { it.toString(16) }
        .map { it.padStart(2, '0') }
        .reduce { acc, s -> acc + s }
