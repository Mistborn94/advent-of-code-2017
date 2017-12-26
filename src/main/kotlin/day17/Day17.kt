package day17

import normalizeIndex

fun main(args: Array<String>) {
    val listA: List<Int> = countUpTo(2017, 312)

    val index2017 = listA.indexOf(2017)
    println("A: ${listA[index2017 + 1]}")

    val answerB: Int = afterZero(5_000_0000, 312)
    println("B: $answerB")
}

fun countUpTo(last: Int, skip: Int): List<Int> {
    val list = ArrayList<Int>(last + 1)
    list.add(0)

    var lastIndex = 0
    (1..last).forEach {
        lastIndex = list.normalizeIndex(lastIndex + skip) + 1
        list.add(lastIndex, it)
    }
    return list
}

fun afterZero(last: Int, skip: Int): Int {
    var atPositionOne = 1
    var lastIndex = 0
    (1..last).forEach {
        lastIndex = normalizeIndex(lastIndex + skip, it) + 1

        if (lastIndex == 1) {
            atPositionOne = it
        }
    }
    return atPositionOne
}

fun normalizeIndex(index: Int, size: Int): Int = index % size