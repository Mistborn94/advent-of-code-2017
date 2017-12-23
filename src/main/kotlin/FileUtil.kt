import java.io.File

object FileUtil {
    fun getFileFromClasspath(fileName: String) = File(FileUtil::class.java.getResource(fileName).toURI())
}

fun <T, R> File.mapLines(mapper: (String) -> T, collector: (Sequence<T>) -> R): R = this.useLines { collector(it.map(mapper)) }
fun <R> File.mapLines(mapper: (String) -> R): List<R> = this.mapLines(mapper, { it.toList() })




