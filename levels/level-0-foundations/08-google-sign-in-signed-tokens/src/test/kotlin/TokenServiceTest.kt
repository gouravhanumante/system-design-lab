import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TokenServiceTest {
    private val secret = "super-secret-key-1".toByteArray()

    @Test
    fun tokenFormatIsExact() {
        val t = TokenService(secret).issue("alice", 0)
        val payload = "alice|3600|1"
        assertEquals(b64(payload.toByteArray()) + "." + b64(hmacSha256(secret, payload)), t)
    }

    @Test
    fun verifyReturnsTheUserUntilExpiry() {
        val s = TokenService(secret, ttlSeconds = 3600)
        val t = s.issue("alice", 100)
        assertEquals("alice", s.verify(t, 100))
        assertEquals("alice", s.verify(t, 3699))
        assertNull(s.verify(t, 3700))
    }

    @Test
    fun anyServerWithTheSecretCanVerifyButNotOthers() {
        val t = TokenService(secret).issue("alice", 0)
        assertEquals("alice", TokenService(secret).verify(t, 1))           // another "server", no shared state
        assertNull(TokenService("other-secret".toByteArray()).verify(t, 1))
    }

    @Test
    fun tamperingBreaksTheSignature() {
        val s = TokenService(secret)
        val t = s.issue("alice", 0)
        val sig = t.substringAfter(".")
        val forged = b64("admin|3600|1".toByteArray()) + "." + sig
        assertNull(s.verify(forged, 1))
        val longerLife = b64("alice|999999|1".toByteArray()) + "." + sig
        assertNull(s.verify(longerLife, 1))
    }

    @Test
    fun garbageNeverThrows() {
        val s = TokenService(secret)
        for (g in listOf("", ".", "abc", "a.b.c", "!!!.???", "YWxpY2U.", ".YWxpY2U")) assertNull(s.verify(g, 0), g)
        val badPayload = "not-three-fields"
        assertNull(s.verify(b64(badPayload.toByteArray()) + "." + b64(hmacSha256(secret, badPayload)), 0))
        val badNumber = "alice|soon|1"
        assertNull(s.verify(b64(badNumber.toByteArray()) + "." + b64(hmacSha256(secret, badNumber)), 0))
    }

    @Test
    fun revocation() {
        val s = TokenService(secret)
        val t1 = s.issue("alice", 0)
        val t2 = s.issue("alice", 0)
        assertTrue(s.revoke(t1))
        assertNull(s.verify(t1, 1))
        assertEquals("alice", s.verify(t2, 1))                           // only that token is dead
        assertFalse(s.revoke("garbage"))
        assertFalse(s.revoke(b64("bob|3600|7".toByteArray()) + "." + t1.substringAfter(".")))
    }

    @Test
    fun tokenIdsIncrease() {
        val s = TokenService(secret)
        s.issue("a", 0)
        val t = s.issue("b", 0)
        assertEquals("b|3600|2", String(unb64(t.substringBefore("."))!!))
    }

    @Test
    fun invalidUserIds() {
        val s = TokenService(secret)
        assertFailsWith<IllegalArgumentException> { s.issue("", 0) }
        assertFailsWith<IllegalArgumentException> { s.issue("al|ice", 0) }
    }
}
