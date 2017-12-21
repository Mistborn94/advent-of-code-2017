package day9

fun main(args: Array<String>) {
    val fileFromClasspath = FileUtil.getFileFromClasspath("Day9.txt")

    val result = sumGroup(fileFromClasspath.readText(), 0)

    println("A: ${result.score}")
    println("B: ${result.garbageCount}")
}

data class StreamStats(val length: Int, val score: Int, val garbageCount: Int = 0) {
//    operator fun plus(other: StreamStats): StreamStats = StreamStats(score + other.score, length + other.length, garbageCount + other.garbageCount)
}

fun sumGroup(readText: String, depth: Int = 0): StreamStats {
    var currentIndex = 1
    var currentScore = depth + 1
    var garbageCount = 0

    while (readText[currentIndex] != '}') {
        when (readText[currentIndex]) {
            '<' -> {
                var cancelCount = 0
                var newIndex = currentIndex
                while (readText[newIndex] != '>') {
                    val nextIndex = readText.indexOfAny(charArrayOf('!', '>'), newIndex)
                    val charAt = readText[nextIndex]

                    newIndex = nextIndex

                    if (charAt == '!') {
                        cancelCount++
                        newIndex += 2
                    }
                }
                garbageCount += newIndex - currentIndex - cancelCount * 2 - 1
                currentIndex = newIndex + 1
            }
            '{' -> {
                val sumGroup = sumGroup(readText.substring(currentIndex), depth + 1)
                currentIndex += sumGroup.length
                currentScore += sumGroup.score
                garbageCount += sumGroup.garbageCount
            }
            ',' -> {
                currentIndex++
            }
            else -> {
                throw Exception("Unexpected character ${readText[currentIndex]}")
            }
        }
    }

    return StreamStats(currentIndex + 1, currentScore, garbageCount)
}


