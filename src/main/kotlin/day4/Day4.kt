package day4

import FileUtil
import java.io.File

fun main(args: Array<String>) {
    val fileFromClasspath = FileUtil.getFileFromClasspath("Day4.txt")

    val totalA = getValidLines(fileFromClasspath, ::isValidUnique)
    val totalB = getValidLines(fileFromClasspath, ::isValidAnagram)
    println("A: $totalA")
    println("B: $totalB")
}

private fun getValidLines(fileFromClasspath: File, action: (String) -> Boolean): Int {
    var total = 0
    fileFromClasspath.forEachLine {
        total += if (action(it)) 1 else 0
    }
    return total
}

fun isValidUnique(input: String): Boolean {
    val words: MutableSet<String> = HashSet()

    for (word in input.split("\\s".toRegex())) {
        if (words.contains(word)) {
            return false
        }
        words.add(word)
    }

    return true
}

fun isValidAnagram(input: String): Boolean {
    val words: MutableSet<List<Char>> = HashSet()

    for (word in input.split("\\s".toRegex())) {
        val normalizedWord = word.toCharArray().sorted()
        if (words.contains(normalizedWord)) {
            return false
        }
        words.add(normalizedWord)
    }

    return true
}
