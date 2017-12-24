package day15

fun main(args: Array<String>) {

    val initialA: Long = 277
    val initialB: Long = 349
    println("A ${countBasicMatching(initialA, initialB, 40_000_000)}")
    println("B ${countAdvancedMatching(initialA, initialB, 5_000_000)}")
}

val factorA: Long = 16807
val factorB: Long = 48271
val bitmask = 65535L // 2^16 - 1

fun countBasicMatching(initialA: Long, initialB: Long, reps: Int): Int {
    val genA = buildSequence(initialA, factorA).take(reps + 1)
    val genB = buildSequence(initialB, factorB).take(reps + 1)

    return duel(genA, genB)
}

private fun duel(genA: Sequence<Long>, genB: Sequence<Long>): Int =
        genA.zip(genB).count { (valA, valB) ->
            valA and bitmask == valB and bitmask
        }

fun countAdvancedMatching(initialA: Long, initialB: Long, reps: Int): Int {
    val genA = buildSequence(initialA, factorA).filter { it % 4 == 0L }.take(reps)
    val genB = buildSequence(initialB, factorB).filter { it % 8 == 0L }.take(reps)

    return duel(genA, genB)
}

fun buildSequence(initial: Long, factor: Long): Sequence<Long> {
    return generateSequence(initial, { (it * factor) % Int.MAX_VALUE }).drop(1)
}