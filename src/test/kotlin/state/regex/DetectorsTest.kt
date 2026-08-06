package state.regex

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DetectorsTest {
    private val integerDetector = IntegerDetector()
    private val floatingPointDetector = FloatingPointDetector()
    private val binaryOnesDetector = BinaryOnesDetector()
    private val emailAddressDetector = EmailAddressDetector()
    private val passwordDetector = PasswordDetector()

    @Test
    fun integerValidExamples() {
        assertTrue(integerDetector.detect("1"))
        assertTrue(integerDetector.detect("123"))
        assertTrue(integerDetector.detect("3452342352434534524346"))
    }

    @Test
    fun integerInvalidExamples() {
        assertFalse(integerDetector.detect(""))
        assertFalse(integerDetector.detect("0123"))
        assertFalse(integerDetector.detect("132a"))
        assertFalse(integerDetector.detect("0"))
    }

    @Test
    fun integerAdditionalTrickyCases() {
        assertTrue(integerDetector.detect("9"))
        assertTrue(integerDetector.detect("10"))

        assertFalse(integerDetector.detect("01"))
        assertFalse(integerDetector.detect("-1"))
        assertFalse(integerDetector.detect("+1"))
        assertFalse(integerDetector.detect("1 2"))
        assertFalse(integerDetector.detect("1.2"))
    }

    @Test
    fun floatingPointValidExamples() {
        assertTrue(floatingPointDetector.detect("1.0"))
        assertTrue(floatingPointDetector.detect("123.34"))
        assertTrue(floatingPointDetector.detect("0.20000"))
        assertTrue(floatingPointDetector.detect("12349871234.12340981234098"))
        assertTrue(floatingPointDetector.detect(".123"))
    }

    @Test
    fun floatingPointInvalidExamples() {
        assertFalse(floatingPointDetector.detect("123"))
        assertFalse(floatingPointDetector.detect("123.123."))
        assertFalse(floatingPointDetector.detect("123.02a"))
        assertFalse(floatingPointDetector.detect("123."))
        assertFalse(floatingPointDetector.detect("012.4"))
    }

    @Test
    fun floatingPointAdditionalTrickyCases() {
        assertTrue(floatingPointDetector.detect("0.0"))
        assertTrue(floatingPointDetector.detect("9.999"))
        assertTrue(floatingPointDetector.detect(".0"))

        assertFalse(floatingPointDetector.detect(""))
        assertFalse(floatingPointDetector.detect("0"))
        assertFalse(floatingPointDetector.detect("."))
        assertFalse(floatingPointDetector.detect("00.1"))
        assertFalse(floatingPointDetector.detect("1..0"))
        assertFalse(floatingPointDetector.detect("1.2.3"))
        assertFalse(floatingPointDetector.detect("1a.2"))
        assertFalse(floatingPointDetector.detect(" 1.2"))
        assertFalse(floatingPointDetector.detect("1.2 "))
    }

    @Test
    fun binaryValidExamples() {
        assertTrue(binaryOnesDetector.detect("1"))
        assertTrue(binaryOnesDetector.detect("11"))
        assertTrue(binaryOnesDetector.detect("101"))
        assertTrue(binaryOnesDetector.detect("111111"))
        assertTrue(binaryOnesDetector.detect("10011010001"))
    }

    @Test
    fun binaryInvalidExamples() {
        assertFalse(binaryOnesDetector.detect("01"))
        assertFalse(binaryOnesDetector.detect("10"))
        assertFalse(binaryOnesDetector.detect("1000010"))
        assertFalse(binaryOnesDetector.detect("100a01"))
    }

    @Test
    fun binaryAdditionalTrickyCases() {
        assertTrue(binaryOnesDetector.detect("1010101"))

        assertFalse(binaryOnesDetector.detect(""))
        assertFalse(binaryOnesDetector.detect("0"))
        assertFalse(binaryOnesDetector.detect("2"))
        assertFalse(binaryOnesDetector.detect("1110"))
        assertFalse(binaryOnesDetector.detect(" 101"))
        assertFalse(binaryOnesDetector.detect("101 "))
    }

    @Test
    fun emailValidExamples() {
        assertTrue(emailAddressDetector.detect("a@b.c"))
        assertTrue(emailAddressDetector.detect("joseph.ditton@usu.edu"))
        assertTrue(emailAddressDetector.detect("{}*$.&$*(@*$%&.*&*"))
    }

    @Test
    fun emailInvalidExamples() {
        assertFalse(emailAddressDetector.detect("@b.c"))
        assertFalse(emailAddressDetector.detect("a@b@c.com"))
        assertFalse(emailAddressDetector.detect("a.b@b.b.c"))
        assertFalse(emailAddressDetector.detect("joseph ditton@usu.edu"))
    }

    @Test
    fun emailAdditionalTrickyCases() {
        assertTrue(emailAddressDetector.detect("ab@cd.ef"))
        assertTrue(emailAddressDetector.detect("a+b@b-c.d_e"))

        assertFalse(emailAddressDetector.detect(""))
        assertFalse(emailAddressDetector.detect("a@.c"))
        assertFalse(emailAddressDetector.detect("a@b."))
        assertFalse(emailAddressDetector.detect("a@b.c.d"))
        assertFalse(emailAddressDetector.detect("a@b..c"))
        assertFalse(emailAddressDetector.detect("a@@b.c"))
        assertFalse(emailAddressDetector.detect(" a@b.c"))
        assertFalse(emailAddressDetector.detect("a@b.c "))
    }

    @Test
    fun passwordValidExamples() {
        assertTrue(passwordDetector.detect("aaaaH!aa"))
        assertTrue(passwordDetector.detect("1234567*9J"))
        assertTrue(passwordDetector.detect("asdpoihj;loikjasdf;ijp;lij2309jasd;lfkm20ij@aH"))
    }

    @Test
    fun passwordInvalidExamples() {
        assertFalse(passwordDetector.detect("a"))
        assertFalse(passwordDetector.detect("aaaaaaa!"))
        assertFalse(passwordDetector.detect("aaaHaaaaa"))
        assertFalse(passwordDetector.detect("Abbbbbbb!"))
    }

    @Test
    fun passwordAdditionalTrickyCases() {
        assertTrue(passwordDetector.detect("A!aaaaa1"))
        assertTrue(passwordDetector.detect("aaaaaa!A"))
        assertTrue(passwordDetector.detect("A!aaaaa1!b"))

        assertFalse(passwordDetector.detect("A!aaaa1"))
        assertFalse(passwordDetector.detect("Abcdefg?1"))
        assertFalse(passwordDetector.detect("ABCDEFGH!"))
        assertFalse(passwordDetector.detect("12345678*"))
        assertFalse(passwordDetector.detect("Aa1!aaaa*"))
        assertFalse(passwordDetector.detect("A!aaaaa1!"))
    }
}
