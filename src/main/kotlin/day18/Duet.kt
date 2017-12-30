package day18

import java.util.*

class Duet(internal val programId: Int, other: Duet?) {
    private val instructions = mutableListOf<Instruction>()
    private val registers = mutableMapOf<Char, Long>()
    private val send: Queue<Long>
    private val receive: Queue<Long>

    constructor(programId: Int = 0) : this(programId, null)

    init {
        registers.put('p', programId.toLong())

        if (other == null) {
            send = ArrayDeque()
            receive = ArrayDeque()
        } else {
            send = other.receive
            receive = other.send
        }
    }

    private var lastSoundFrequency = 0L
    var currentInstruction = 0L
    var sendCount = 0
    val terminated get() = currentInstruction < 0 || currentInstruction > instructionCount

    fun receive(register: Char): Boolean {
        return if (receive.isNotEmpty()) {
            val value = receive.poll()
            setRegisterValue(register, value)
            true
        } else {
            false
        }
    }

    fun send(register: Char) {
        send.offer(getRegisterValue(register))
        sendCount++
    }

    fun getRegisterValue(register: Char) = registers.getOrDefault(register, 0)

    fun setRegisterValue(register: Char, value: Long) {
        registers.put(register, value)
    }

    fun playSound(value: Long) {
        lastSoundFrequency = value
    }

    fun addPart1Instruction(name: String, register: Char, value: String?) {
        val instruction = when (name) {
            "snd" -> PlaySound(register, this)
            "set" -> SetRegister(register, value!!, this)
            "add" -> Add(register, value!!, this)
            "mul" -> Multiply(register, value!!, this)
            "mod" -> Modulus(register, value!!, this)
            "rcv" -> Recover(register, this)
            "jgz" -> Jump(register, value!!, this)
            else -> throw Exception("Unknown instruction $name")
        }
        instructions.add(instruction)
    }

    fun addPart2Instruction(name: String, first: Char, second: String?) {
        val instruction = when (name) {
            "snd" -> Send(first, this)
            "set" -> SetRegister(first, second!!, this)
            "add" -> Add(first, second!!, this)
            "mul" -> Multiply(first, second!!, this)
            "mod" -> Modulus(first, second!!, this)
            "rcv" -> Receive(first, this)
            "jgz" -> Jump(first, second!!, this)
            else -> throw Exception("Unknown instruction $name")
        }
        instructions.add(instruction)
    }

    fun incrementCurrentInstruction() {
        currentInstruction++
    }

    fun evaluatePart1(): Long {
        registers.clear()
        currentInstruction = 0
        lastSoundFrequency = 0

        var activeInstruction = instructions[currentInstruction.toInt()]
        while (activeInstruction.evaluate()) {
            activeInstruction = instructions[currentInstruction.toInt()]
        }
        return lastSoundFrequency
    }

    val instructionCount: Int get() = instructions.size

    fun evaluateCurrentInstruction(): Boolean = !terminated && instructions[currentInstruction.toInt()].evaluate()

    fun evaluateInstruction(valueOrRegister: String): () -> Long {
        return if (valueOrRegister.length == 1 && valueOrRegister[0] in 'a'..'z') {
            { getRegisterValue(valueOrRegister[0]) }
        } else {
            { valueOrRegister.toLong() }
        }
    }

    fun evaluateInstruction(valueOrRegister: Char): () -> Long {
        return if (valueOrRegister in 'a'..'z') {
            { getRegisterValue(valueOrRegister) }
        } else {
            val resultValue = (valueOrRegister - '0').toLong();
            { resultValue }
        }
    }
}

class DoubleDuet {
    val program0 = Duet(0)
    val program1 = Duet(1, program0)

    fun addPart2Instruction(name: String, register: Char, value: String?) {
        program0.addPart2Instruction(name, register, value)
        program1.addPart2Instruction(name, register, value)
    }

    fun evaluatePart2(): Int {
        var program0Success = program0.evaluateCurrentInstruction()
        var program1Success = program1.evaluateCurrentInstruction()

        var loopIndex = 0
        while (program0Success || program1Success) {
            loopIndex++
            program0Success = program0.evaluateCurrentInstruction()
            program1Success = program1.evaluateCurrentInstruction()
        }

        return program1.sendCount
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

    val instructionValue = duet.evaluateInstruction(valueString)
    val registerValue: () -> Long = { duet.getRegisterValue(register) }

    protected fun setAndContinue(newValue: Long): Boolean {
        duet.setRegisterValue(register, newValue)
        return continueToNext()
    }
}

class PlaySound(private val register: Char, duet: Duet) : Instruction(duet) {

    private val registerValue: () -> Long = { duet.getRegisterValue(register) }

    override fun evaluate(): Boolean {
        duet.playSound(registerValue())
        return super.continueToNext()
    }

}

class SetRegister(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(instructionValue())
}

class Add(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(registerValue() + instructionValue())
}

class Multiply(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(registerValue() * instructionValue())
}

class Modulus(register: Char, instruction: String, duet: Duet) : RegisterChangeInstruction(duet, register, instruction) {

    override fun evaluate(): Boolean = setAndContinue(registerValue() % instructionValue())
}

class Recover(register: Char, duet: Duet) : Instruction(duet) {
    private val registerValue: () -> Long = { duet.getRegisterValue(register) }

    override fun evaluate(): Boolean = if (registerValue() != 0L) {
        false
    } else {
        super.continueToNext()
    }
}

class Receive(private val register: Char, duet: Duet) : Instruction(duet) {

    override fun evaluate(): Boolean {
        val success = duet.receive(register)
        if (success) {
            super.continueToNext()
        }
        return success
    }
}

class Send(private val register: Char, duet: Duet) : Instruction(duet) {

    override fun evaluate(): Boolean {
        duet.send(register)
        return super.continueToNext()
    }
}

class Jump(check: Char, jump: String, duet: Duet) : Instruction(duet) {
    private val checkValue = duet.evaluateInstruction(check)
    private val jumpValue = duet.evaluateInstruction(jump)

    override fun evaluate(): Boolean = if (checkValue() > 0L) {
        duet.currentInstruction += jumpValue()
        !duet.terminated
    } else {
        super.continueToNext()
    }
}



