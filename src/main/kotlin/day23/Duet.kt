package day23

class Duet {
    private val instructions = mutableListOf<Instruction>()
    private val registers = mutableMapOf<Char, Long>()

    private var lastSoundFrequency = 0L
    private val instructionCount: Int get() = instructions.size
    var currentInstruction = 0L
    val terminated get() = currentInstruction < 0 || currentInstruction >= instructionCount

    fun getRegisterValue(register: Char) = registers.getOrDefault(register, 0)

    fun setRegisterValue(register: Char, value: Long) {
        registers[register] = value
    }

    fun setInstructions(lines: List<String>) {
        instructions.clear()
        lines.forEach {
            val (name, a, b) = it.split(" ")
            addInstruction(name, a[0], b)
        }
    }


    private fun addInstruction(name: String, register: Char, value: String) {
        val instruction = when (name) {
            "set" -> SetRegister(register, value, this)
            "sub" -> Subtract(register, value, this)
            "mul" -> Multiply(register, value, this)
            "jnz" -> Jump(register, value, this)
            else -> throw Exception("Unknown instruction $name")
        }
        instructions.add(instruction)
    }

    fun incrementCurrentInstruction() {
        currentInstruction++
    }

    fun evaluateB(): Long {
        evaluate(mapOf('a' to 1))
        return registers['h']!!
    }

    fun evaluate(initialRegisters: Map<Char, Long> = emptyMap()): Long {
        registers.clear()
        registers.putAll(initialRegisters)
        currentInstruction = 0
        lastSoundFrequency = 0
        var iteration = 0L
        var multiplyCount = 0L

        var activeInstruction = instructions[currentInstruction.toInt()]
        while (activeInstruction.evaluate()) {
            iteration++
            if (iteration % 10_000 == 0L) {
                println("Iteration $iteration - $registers")
            }
            if (activeInstruction is Multiply) {
                multiplyCount++
            }
            activeInstruction = instructions[currentInstruction.toInt()]

            if (activeInstruction is Jump && activeInstruction.check != 'a') {
                registers[activeInstruction.check] = 0
            }
        }
        return multiplyCount
    }


    fun valueOrRegister(valueOrRegister: String): () -> Long {
        return if (valueOrRegister.length == 1 && valueOrRegister[0] in 'a'..'h') {
            { getRegisterValue(valueOrRegister[0]) }
        } else {
            { valueOrRegister.toLong() }
        }
    }

    fun valueOrRegister(valueOrRegister: Char): () -> Long {
        return if (valueOrRegister in 'a'..'h') {
            { getRegisterValue(valueOrRegister) }
        } else {
            val resultValue = (valueOrRegister - '0').toLong();
            { resultValue }
        }
    }
}

abstract class Instruction(val duet: Duet) {

    abstract fun evaluate(): Boolean

    fun continueToNext(): Boolean {
        duet.incrementCurrentInstruction()
        return !duet.terminated
    }
}

abstract class RegisterChangeInstruction(duet: Duet, private val register: Char, valueString: String) : Instruction(duet) {

    val instructionValue = duet.valueOrRegister(valueString)
    val registerValue = { duet.getRegisterValue(register) }

    protected fun setAndContinue(newValue: Long): Boolean {
        duet.setRegisterValue(register, newValue)
        return continueToNext()
    }
}

class SetRegister(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(instructionValue())
}

class Subtract(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(registerValue() - instructionValue())
}

class Multiply(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(registerValue() * instructionValue())
}


class Jump(val check: Char, val jump: String, duet: Duet) : Instruction(duet) {
    private val checkValue = duet.valueOrRegister(check)
    private val jumpValue = jump.toInt()

    override fun evaluate(): Boolean = if (checkValue() != 0L) {
        duet.currentInstruction += jumpValue
        !duet.terminated
    } else {
        super.continueToNext()
    }
}



