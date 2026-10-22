import kotlin.test.Test
import kotlin.test.assertEquals

class OtpServiceTest {
    private fun service(vararg codes: String, ttl: Long = 600, max: Int = 3): OtpService {
        val q = ArrayDeque(codes.toList())
        return OtpService(ttl, max) { q.removeFirst() }
    }

    @Test
    fun correctCodeWithinTtlIsOk() {
        val s = service("1234")
        assertEquals("1234", s.issue("r", 0))
        assertEquals(VerifyResult.OK, s.verify("r", "1234", 599))
    }

    @Test
    fun expiresExactlyAtTtl() {
        val s = service("1234")
        s.issue("r", 0)
        assertEquals(VerifyResult.EXPIRED, s.verify("r", "1234", 600))
    }

    @Test
    fun usedCodeCannotBeReused() {
        val s = service("1234")
        s.issue("r", 0)
        assertEquals(VerifyResult.OK, s.verify("r", "1234", 1))
        assertEquals(VerifyResult.ALREADY_USED, s.verify("r", "1234", 2))
        assertEquals(VerifyResult.ALREADY_USED, s.verify("r", "9999", 3))
    }

    @Test
    fun unknownRide() {
        assertEquals(VerifyResult.UNKNOWN_RIDE, service("1234").verify("ghost", "1234", 0))
    }

    @Test
    fun locksAfterMaxWrongAttemptsEvenForTheRightCode() {
        val s = service("1234")
        s.issue("r", 0)
        assertEquals(VerifyResult.WRONG, s.verify("r", "0001", 1))
        assertEquals(VerifyResult.WRONG, s.verify("r", "0002", 2))
        assertEquals(VerifyResult.WRONG, s.verify("r", "0003", 3))
        assertEquals(VerifyResult.LOCKED, s.verify("r", "1234", 4))
        assertEquals(VerifyResult.LOCKED, s.verify("r", "0004", 5))
    }

    @Test
    fun twoWrongAttemptsThenRightCodeStillWorks() {
        val s = service("1234")
        s.issue("r", 0)
        s.verify("r", "x", 1); s.verify("r", "y", 2)
        assertEquals(VerifyResult.OK, s.verify("r", "1234", 3))
    }

    @Test
    fun expiredAttemptsDoNotCountAsWrongGuesses() {
        val s = service("1234", "5678")
        s.issue("r", 0)
        repeat(5) { assertEquals(VerifyResult.EXPIRED, s.verify("r", "bad", 1000 + it.toLong())) }
        assertEquals("5678", s.issue("r", 2000))
        assertEquals(VerifyResult.OK, s.verify("r", "5678", 2001))   // not locked: expired calls were free
    }

    @Test
    fun lockedTakesPriorityOverExpired() {
        val s = service("1234", max = 1)
        s.issue("r", 0)
        assertEquals(VerifyResult.WRONG, s.verify("r", "bad", 1))
        assertEquals(VerifyResult.LOCKED, s.verify("r", "1234", 99999))
    }

    @Test
    fun reissueReplacesTheOldCodeAndClearsLockAndUsedFlags() {
        val s = service("1111", "2222", "3333", max = 2)
        s.issue("r", 0)
        s.verify("r", "bad", 1); s.verify("r", "bad", 2)
        assertEquals(VerifyResult.LOCKED, s.verify("r", "1111", 3))
        assertEquals("2222", s.issue("r", 10))
        assertEquals(VerifyResult.WRONG, s.verify("r", "1111", 11))          // old code is dead
        assertEquals(VerifyResult.OK, s.verify("r", "2222", 12))
        assertEquals("3333", s.issue("r", 20))                                // reissue after use
        assertEquals(VerifyResult.OK, s.verify("r", "3333", 21))
    }

    @Test
    fun reissueExtendsTheDeadline() {
        val s = service("1111", "2222")
        s.issue("r", 0)
        s.issue("r", 500)
        assertEquals(VerifyResult.OK, s.verify("r", "2222", 1099))
    }
}
