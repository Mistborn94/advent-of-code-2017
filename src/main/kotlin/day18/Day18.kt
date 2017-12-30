package day18


fun main(args: Array<String>) {
    val duet = Duet()
    val duet2 = DoubleDuet()
    FileUtil.getFileFromClasspath("Day18.txt").forEachLine {
        val name = it.substring(0, 3)
        val register = it[4]
        val value = if (it.length > 6) it.substring(6) else null
        duet.addPart1Instruction(name, register, value)
        duet2.addPart2Instruction(name, register, value)
    }

    println("A: ${duet.evaluatePart1()}")
    println("B: ${duet2.evaluatePart2()}")
}