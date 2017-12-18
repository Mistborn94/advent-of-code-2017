package day5

fun main(args: Array<String>) {
    val fileFromClasspath = Util.getFileFromClasspath("Day5.txt")
    val maze = fileFromClasspath.readLines().map { it.toInt() }.toTypedArray()
    println("A: " + getBasicStepCount(maze))
    println("B: " + getAlternateStepCount(maze))
}

fun getStepCount(originalMaze: Array<Int>, getNewValue: (Int) -> Int): Int {
    val maze = originalMaze.clone()

    var index = 0
    var stepCount = 0
    while (maze.indices.contains(index)) {
        val value = maze[index]
        maze[index] = getNewValue(value)
        index += value
        stepCount++
    }

    return stepCount
}

fun getBasicStepCount(originalMaze: Array<Int>): Int {
    return getStepCount(originalMaze, { it + 1 })
}

fun getAlternateStepCount(originalMaze: Array<Int>): Int {
    return getStepCount(originalMaze, { if (it >= 3) it - 1 else it + 1 })
}
