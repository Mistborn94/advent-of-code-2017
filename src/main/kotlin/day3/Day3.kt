package day3

import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val input = 347991
    println("A: ${findSteps(input)}")
    println("B: ${findNextSum(input)}")
}

typealias LoopIndex = Int

val LoopIndex.size get() = (this * 2.0.pow(3)).toInt()
val LoopIndex.cellBeforeStart get() = (this * 2.0 - 1).pow(2).toInt()
fun LoopIndex.getPositionInLoop(cellIndex: Int) = cellIndex - this.cellBeforeStart

fun findSteps(cellIndex: Int): Int {
    val loopIndex = getLoop(cellIndex)

    if (loopIndex == 0) {
        return 0
    }

    val relativePos = loopIndex.getPositionInLoop(cellIndex)
    val loopSideSize = loopIndex.size / 4

    val stepsMod = relativePos % loopSideSize
    val extraSteps = (stepsMod - loopSideSize / 2).absoluteValue

    return loopIndex + extraSteps
}

private fun getLoop(input: Int): LoopIndex {
    val ceilSqrt = ceil(sqrt(input.toDouble())).toInt()
    return (ceilSqrt - ceilSqrt % 2) / 2
}

fun findNextSum(input: Int): Int {
    val loops = SumLoops()
    while (loops.lastValue <= input) {
        loops.addNextValue()
    }
    return loops.lastValue
}

class SumLoops : MutableIterable<LoopValues> {

    private val loops: MutableList<LoopValues> = mutableListOf(InitialLoop)
    private val activeLoop get() = loops.last()
    private val previousLoop get() = loops[loops.lastIndex - 1]

    override fun iterator() = loops.iterator()

    private fun addNextLoop() {
        loops.add(GeneralLoopValues(loops.size))
    }

    val lastValue get() = activeLoop.last()

    fun addNextValue() {
        if (activeLoop.isFull) {
            addNextLoop()
        }

        val cell = activeLoop.nextCell
        val surrounding = getSurrounding(cell)
        val value = surrounding.sum()
        activeLoop.add(value)
    }

    private fun getSurrounding(cell: LoopValues.Cell): List<Int> {
        val nextCell = cell + 1
        val previousCell = cell - 1

        val surrounding = mutableListOf(
                nextCell.value,
                previousCell.value
        )

        when {
            activeLoop.loopIndex == 1 -> {
                surrounding.add(previousLoop[0])
                if (!cell.isCorner) {
                    surrounding.add((cell - 2).value)
                    surrounding.add((cell + 2).value)
                }
            }

            cell.isCorner -> surrounding.add(previousLoop.getCorner(cell.side))

            nextCell.isCorner -> {
                surrounding.add((cell + 2).value)
                surrounding.add(previousLoop.getCorner(cell.side))
                surrounding.add(previousLoop[cell.side, cell.index - 2])
            }

            previousCell.isCorner -> {
                surrounding.add((cell - 2).value)
                surrounding.add(previousLoop.getCorner(cell.side - 1))
                surrounding.add(previousLoop[cell.side, cell.index])
            }

            else -> {
                surrounding.add(previousLoop[cell.side, cell.index])
                surrounding.add(previousLoop[cell.side, cell.index - 1])
                surrounding.add(previousLoop[cell.side, cell.index - 2])
            }
        }
        return surrounding
    }
}

abstract class LoopValues constructor(val loopIndex: LoopIndex) {

    abstract val isFull: Boolean
    abstract val sideSize: Int
    abstract val nextCell: Cell

    val maxSize = (2.toDouble().pow(3) * loopIndex).toInt()

    abstract operator fun get(index: Int): Int
    abstract operator fun get(side: Int, position: Int): Int

    abstract fun add(value: Int)
    abstract fun last(): Int

    fun getCorner(side: Int) = this[side, sideSize - 1]

    inner class Cell(val side: Int, val index: Int) {
        val overallIndex = side * sideSize + index
        val isCorner = (index + 1) % sideSize == 0
        val value get() = get(overallIndex)

        operator fun plus(value: Int) = Cell(overallIndex + value)
        operator fun minus(value: Int) = Cell(overallIndex - value)

        constructor(arrayIndex: Int) : this(arrayIndex / sideSize, arrayIndex % sideSize)
    }

}

class GeneralLoopValues(loopIndex: LoopIndex) : LoopValues(loopIndex) {
    private val values = IntArray(maxSize)

    override val isFull: Boolean get() = values.last() != 0
    override val nextCell get() = Cell(values.indexOf(0))
    override val sideSize = maxSize / 4

    override operator fun get(index: Int): Int = values[normalizeIndex(index)]
    override operator fun get(side: Int, position: Int): Int = get(Cell(side, position).overallIndex)

    private fun normalizeIndex(index: Int) = if (index < 0) values.size + index else index % values.size

    override fun add(value: Int) {
        values[nextCell.overallIndex] = value
    }

    override fun last(): Int {
        return values.findLast { it != 0 } ?: 0
    }
}

object InitialLoop : LoopValues(0) {
    private val VALUE = 1

    override val isFull = true
    override val sideSize = 1
    override val nextCell = Cell(0)

    override operator fun get(index: Int): Int = VALUE
    override operator fun get(side: Int, position: Int): Int = VALUE

    override fun last(): Int = VALUE
    override fun add(value: Int) {
        throw Exception("Invalid Operation - Loop is full")
    }
}

