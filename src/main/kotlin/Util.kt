import java.io.File

class Util {
    companion object {
        fun getFileFromClasspath(fileName: String) = File(Util::class.java.getResource(fileName).toURI())
    }
}

