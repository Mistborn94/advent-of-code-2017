package day8

import FileUtil
import java.io.File


fun main(args: Array<String>) {
    val fileFromClasspath = FileUtil.getFileFromClasspath("Day8.txt")

    val cpu = Cpu(fileFromClasspath)

    println("A: ${cpu.max}")
    println("B: ${cpu.highestEver}")
}


open class InstructionPart<out T>(val register: String, val operator: (Int, Int) -> T, val value: Int) {
    fun evaluate(registerValue: Int): T {
        return operator(registerValue, value)
    }
}

class InstructionCondition(register: String, operator: String, value: Int)
    : InstructionPart<Boolean>(register, Cpu.conditionOperators[operator]!!, value)

class InstructionValue(register: String, operator: String, value: Int)
    : InstructionPart<Int>(register, Cpu.instructionOperators[operator]!!, value)


class Cpu(file: File) {

    val registerMap = mutableMapOf<String, Int>()
    var highestEver: Int = Int.MIN_VALUE
        private set(value) {
            field = value
        }

    init {
        file.forEachLine { applyInstruction(it) }
    }

    private fun applyInstruction(line: String) {
        val matchResult = linePattern.matchEntire(line)!!.destructured
        CpuInstruction(matchResult).apply()
    }


    val max: Int get() = registerMap.values.max()!!

    inner class CpuInstruction(destructured: MatchResult.Destructured) {
        private val instruction = InstructionValue(destructured.component1(), destructured.component2(), destructured.component3().toInt())
        private val condition = InstructionCondition(destructured.component4(), destructured.component5(), destructured.component6().toInt())

        fun apply() {
            val conditionRegisterValue = registerMap.getOrPut(condition.register, { 0 })

            if (condition.evaluate(conditionRegisterValue)) {
                val instructionRegisterValue = registerMap.getOrDefault(instruction.register, 0)
                val newValue = instruction.evaluate(instructionRegisterValue)

                registerMap.put(instruction.register, newValue)
                if (newValue > highestEver) {
                    highestEver = newValue
                }
            }
        }
    }

    companion object {
        val conditionOperators = mapOf<String, (Int, Int) -> Boolean>(
                Pair(">", { a, b -> a > b }),
                Pair(">=", { a, b -> a >= b }),
                Pair("<", { a, b -> a < b }),
                Pair("<=", { a, b -> a <= b }),
                Pair("==", { a, b -> a == b }),
                Pair("!=", { a, b -> a != b })
        )

        val instructionOperators = mapOf<String, (Int, Int) -> Int>(
                Pair("inc", { a, b -> a + b }),
                Pair("dec", { a, b -> a - b })
        )

        val linePattern = """([a-z]+) (inc|dec) (-?\d+) if ([a-z]+) ([><=!]{1,2}) (-?\d+)""".toRegex()
    }
}