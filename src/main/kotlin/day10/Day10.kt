package day10

val standardSuffix = listOf(17, 31, 73, 47, 23)

fun main(args: Array<String>) {
    val input = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118"
    val result = getBasicHash(input.split(',').map(String::toInt))
    println("A: ${result[0] * result[1]}")

    val denseHash = getAsciiDenseHash(input)
    println("B $denseHash")
}

private fun getBasicHash(lengths: List<Int>): List<Int> {
    val knotA = KnotHash(lengths)
    val result = knotA.finalList
    return result
}

fun getAsciiDenseHash(charSequence: CharSequence): String {
    val lengths = buildLengthsList(charSequence)
    var knotB = KnotHash(lengths)
    repeat(1, { knotB = knotB.next() })
    return knotB.finalList
            .chunked(16)
            .map(List<Int>::xor)
            .toHexString()
}

fun List<Int>.xor(): Int = this.reduce { acc, i -> acc.xor(i) }

fun List<Int>.toHexString(): String {
    return this.map { it.toString(16) }
            .map { it.padStart(2, '0') }
            .reduce { acc, s -> acc + s }
}

fun buildLengthsList(charSequence: CharSequence) = charSequence.map(Char::toInt) + standardSuffix

class KnotHash constructor(val lengths: List<Int>, val listSize: Int = 256, skipSize: Int = 0, startingPosition: Int = 0) {
    val finalList: List<Int>
    val finalSkipSize: Int
    val finalPosition: Int

    init {
        var currentList: CircularList<Int> = CircularList(IntRange(0, listSize - 1).toList())
        var currentSkipSize = skipSize
        var currentPosition = startingPosition

        lengths.forEach { length ->
            currentList = currentList.reverseSection(currentPosition, length)
            val shiftAmount = length + currentSkipSize
            currentPosition = currentList.normalizeIndex(currentPosition + shiftAmount)

            ++currentSkipSize
        }

        finalSkipSize = currentSkipSize
        finalPosition = currentPosition
        finalList = currentList.toList()
    }

    fun next(): KnotHash {
        return KnotHash(lengths, listSize, finalSkipSize, finalPosition)
    }
}

class CircularList<T>(private val underlying: List<T>) : List<T> by underlying {

    override operator fun get(index: Int): T = underlying[normalizeIndex(index)]

    fun normalizeIndex(index: Int): Int {
        val normalized = index % size
        return if (normalized < 0) size + normalized else normalized
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<T> {

        val normalFromIndex = normalizeIndex(fromIndex)
        val normalToIndex = normalizeIndex(toIndex)

        return when {
            normalToIndex > normalFromIndex -> underlying.subList(normalFromIndex, normalToIndex)
            else -> {
                val window1 = underlying.subList(normalFromIndex, size)
                val window2 = underlying.subList(0, normalToIndex)
                window1 + window2
            }
        }
    }

    operator fun plus(elements: Iterable<T>): List<T> {
        return CircularList(underlying + elements)
    }

    fun reverseSection(from: Int, length: Int): CircularList<T> {
        val startIndex = normalizeIndex(from)
        val endIndex = normalizeIndex(startIndex + length)
        val reversed = subList(startIndex, endIndex).reversed()

        return when {
            length <= 1 -> this
            endIndex > startIndex -> {
                val before = underlying.subList(0, startIndex)
                val after = underlying.subList(endIndex, size)
                CircularList(before + reversed + after)
            }
            else -> {
                val between = underlying.subList(endIndex, startIndex)
                val endSize = size - startIndex
                val after = reversed.subList(0, endSize)
                val before = reversed.subList(endSize, reversed.size)
                CircularList(before + between + after)
            }
        }
    }
}


