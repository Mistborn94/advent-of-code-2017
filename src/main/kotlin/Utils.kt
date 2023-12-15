import java.io.File

object FileUtil {
    fun getFileFromClasspath(fileName: String) = File(FileUtil::class.java.getResource(fileName).toURI())
}

fun <T, R> File.mapLines(mapper: (String) -> T, collector: (Sequence<T>) -> R): R = this.useLines { collector(it.map(mapper)) }
fun <R> File.mapLines(mapper: (String) -> R): List<R> = this.mapLines(mapper, { it.toList() })
fun <T> List<T>.normalizeIndex(index: Int): Int {
    val normalized = index % size
    return if (normalized < 0) size + normalized else normalized
}

fun <A, B> Iterable<A>.cartesianProduct(other: Iterable<B>): List<Pair<A, B>> = flatMap { a -> other.map { b -> a to b } }
fun <A, B, R> Iterable<A>.cartesianProduct(other: Iterable<B>, mapper: (A, B) -> R): List<R> = flatMap { a -> other.map { b -> mapper(a, b) } }