package day9

fun main(args: Array<String>) {
    val fileFromClasspath = FileUtil.getFileFromClasspath("Day9.txt")

    val count = sumAllGroups(fileFromClasspath.readText())

    println("A: ${count}")
//    println("B: ${cpu.highestEver}")
}

fun sumAllGroups(readText: String): Int {
    return sumGroup(readText, 0).score
}

data class StreamStats(val length: Int, val score: Int, val garbageCount: Int = 0) {
//    operator fun plus(other: StreamStats): StreamStats = StreamStats(score + other.score, length + other.length, garbageCount + other.garbageCount)
}

//Pair<groupLength, Score>
fun sumGroup(readText: String, depth: Int): StreamStats {
    var currentIndex = 1
    var currentScore = depth + 1
    var garbageCount = 0

    while (readText[currentIndex] != '}') {
        when (readText[currentIndex]) {
            '<' -> {
                var newIndex = currentIndex
                while (readText[newIndex] != '>') {
                    val nextIndex = readText.indexOfAny(charArrayOf('!', '>'), newIndex)
                    val charAt = readText[nextIndex]

                    newIndex = if (charAt == '!') {
                        nextIndex + 2
                    } else {
                        nextIndex
                    }
                }
                currentIndex = newIndex + 1
            }
            '{' -> {
                println("{ @$depth ${readText.substring(currentIndex)}")
                val sumGroup = sumGroup(readText.substring(currentIndex), depth + 1)
                currentIndex += sumGroup.length
                currentScore += sumGroup.score
            }
            ',' -> {
                currentIndex++
            }
            else -> {
                throw Exception("Unexpected character ${readText[currentIndex]}")
            }
        }
    }

    return StreamStats(currentIndex + 1, currentScore)
}


