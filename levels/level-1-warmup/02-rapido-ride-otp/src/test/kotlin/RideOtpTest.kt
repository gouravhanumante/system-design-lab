import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RideOtpTest {
    private fun otpWith(vararg codes: String): Pair<RideOtp, () -> Int> {
        val queue = ArrayDeque(codes.toList())
        var calls = 0
        val otp = RideOtp { calls++; queue.removeFirst() }
        return otp to { calls }
    }

    @Test
    fun issueReturnsGeneratedCodeAndIsStable() {
        val (otp, calls) = otpWith("4821", "9999")
        assertEquals("4821", otp.issue("r1"))
        assertEquals("4821", otp.issue("r1"))
        assertEquals(1, calls())
    }

    @Test
    fun rightCodeWorksOnce() {
        val (otp, _) = otpWith("4821")
        otp.issue("r1")
        assertTrue(otp.verify("r1", "4821"))
        assertFalse(otp.verify("r1", "4821"))
    }

    @Test
    fun wrongCodeFailsAndDoesNotBurnTheRealOne() {
        val (otp, _) = otpWith("4821")
        otp.issue("r1")
        assertFalse(otp.verify("r1", "0000"))
        assertFalse(otp.verify("r1", "4821 "))
        assertTrue(otp.verify("r1", "4821"))
    }

    @Test
    fun unknownRideNeverVerifies() {
        val (otp, _) = otpWith("1111")
        assertFalse(otp.verify("ghost", "1111"))
    }

    @Test
    fun ridesAreIndependent() {
        val (otp, _) = otpWith("1111", "2222")
        otp.issue("a"); otp.issue("b")
        assertFalse(otp.verify("a", "2222"))
        assertTrue(otp.verify("b", "2222"))
        assertTrue(otp.verify("a", "1111"))
    }
}
