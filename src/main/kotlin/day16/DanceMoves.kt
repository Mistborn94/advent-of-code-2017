package day16

sealed class DanceMove {
    companion object {
        private val PARTNER_PATTERN = """p(.)/(.)""".toRegex()
        private val EXCHANGE_PATTERN = """x(\d+)/(\d+)""".toRegex()

        fun getMove(step: String): DanceMove {
            return when (step[0]) {
                's' -> SpinMove(step.substring(1).toInt())
                'p' -> {
                    val (a, b) = PARTNER_PATTERN.matchEntire(step)!!.destructured
                    PartnerMove(a[0], b[0])
                }
                'x' -> {
                    val (a, b) = EXCHANGE_PATTERN.matchEntire(step)!!.destructured
                    ExchangeMove(a.toInt(), b.toInt())
                }
                else -> throw Exception("Unknown Step ${step[0]}")
            }
        }
    }

    abstract fun apply(list: MutableList<Char>)
}

class ExchangeMove(private val first: Int, private val second: Int) : DanceMove() {
    override fun apply(list: MutableList<Char>) {
        list.swap(first, second)
    }
}

class PartnerMove(private val first: Char, private val second: Char) : DanceMove() {
    override fun apply(list: MutableList<Char>) {
        list.partner(first, second)
    }
}
class SpinMove(private val spinAmount: Int) : DanceMove() {
    override fun apply(list: MutableList<Char>) {
        list.spin(spinAmount)
    }
}

fun <E> MutableList<E>.swap(ai: Int, bi: Int) {
    val tmp = this[ai]
    this[ai] = this[bi]
    this[bi] = tmp
}

private fun <T> MutableList<T>.partner(a: T, b: T) {
    this.swap(indexOf(a), indexOf(b))
}

private fun <T> MutableList<T>.spin(amount: Int) {
    val list = this.toList()
    for (index in indices) {
        val newIndex = size + index - amount % size
        this[index] = list[newIndex % size]
    }
}

