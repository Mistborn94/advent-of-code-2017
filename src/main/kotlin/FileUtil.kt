import java.io.File

object FileUtil {
    fun getFileFromClasspath(fileName: String) = File(FileUtil::class.java.getResource(fileName).toURI())
}


