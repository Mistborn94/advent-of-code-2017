package day25

val startStateRegex = "Begin in state ([A-F])\\.\nPerform a diagnostic checksum after (\\d+) steps\\.".toRegex(RegexOption.MULTILINE)
val stateRulesRegex = """^In state ([A-F]):$
^ +If the current value is 0:$
^ +- Write the value ([01]).$
^ +- Move one slot to the (left|right).$
^ +- Continue with state ([A-F]).$
^ +If the current value is 1:$
^ +- Write the value ([01]).$
^ +- Move one slot to the (left|right).$
^ +- Continue with state ([A-F]).$""".toRegex(RegexOption.MULTILINE)

data class ValueRule(val newValue: Boolean, val move: Int, val newState: Char)
data class StateRule(val falseState: ValueRule, val trueState: ValueRule) {

    fun evaluate(currentValue: Boolean): ValueRule = if (currentValue) {
        trueState
    } else {
        falseState
    }
}

fun solve(text: String): Int {
    val chunks = text.split("\n\n")
    val (startState, stepCount) = startStateRegex.matchEntire(chunks[0].trim())!!.destructured

    val states = chunks.drop(1).associate {
        val match = stateRulesRegex.matchEntire(it)!!
        val state = match.groupValues[1].first()
        val falseRule = ValueRule(parseBinaryBoolean(match.groupValues[2]), parseDirection(match.groupValues[3]), match.groupValues[4].first())
        val trueRule = ValueRule(parseBinaryBoolean(match.groupValues[5]), parseDirection(match.groupValues[6]), match.groupValues[7].first())

        state to StateRule(falseRule, trueRule)
    }

    val trueValues = mutableSetOf<Int>()
    var position = 0
    var currentState = startState.first()

    repeat(stepCount.toInt()) {
        val currentValue = trueValues.remove(position)
        val stateRule = states[currentState]!!
        val result = stateRule.evaluate(currentValue)
        if (result.newValue) {
            trueValues.add(position)
        }
        position += result.move
        currentState = result.newState
    }

    return trueValues.size
}

fun parseDirection(s: String): Int {
    return if (s == "left") -1 else 1
}

fun parseBinaryBoolean(s: String) = s != "0"

val sampleText = """
Begin in state A.
Perform a diagnostic checksum after 6 steps.

In state A:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state B.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the left.
    - Continue with state B.

In state B:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state A.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.
""".trimIndent()

fun main(args: Array<String>) {

    val text = FileUtil.getFileFromClasspath("Day25.txt").readText().trimEnd()

    println("Sample 1 [3]: ${solve(sampleText)}")
    println("Solve [4387]: ${solve(text)}")
}