package day1

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class Day1KtTest {

    @Test
    fun testGetCaptcha() {
        assertAll("A",
                Executable { assertEquals(3, getCaptcha("1122")) },
                Executable { assertEquals(4, getCaptcha("1111")) },
                Executable { assertEquals(0, getCaptcha("1234")) },
                Executable { assertEquals(9, getCaptcha("91212129")) }
        )

        assertAll("B",
                Executable { assertEquals(6, getCaptchaB("1212")) },
                Executable { assertEquals(0, getCaptchaB("1221")) },
                Executable { assertEquals(4, getCaptchaB("123425")) },
                Executable { assertEquals(12, getCaptchaB("123123")) },
                Executable { assertEquals(4, getCaptchaB("12131415")) }
        )


    }
}
