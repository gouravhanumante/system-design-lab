import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/** Provided: HMAC-SHA256 of [message] with [secret]. */
fun hmacSha256(secret: ByteArray, message: String): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(SecretKeySpec(secret, "HmacSHA256"))
    return mac.doFinal(message.toByteArray(Charsets.UTF_8))
}

/** Provided: URL-safe base64 without padding. */
fun b64(bytes: ByteArray): String = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)

/** Provided: decode URL-safe base64, or null if invalid. */
fun unb64(s: String): ByteArray? = try { Base64.getUrlDecoder().decode(s) } catch (e: IllegalArgumentException) { null }

/** What a valid token contains once decoded. */
private data class TokenData(val userId: String, val expiresAt: Long, val tokenId: Long)

class TokenService(private val secret: ByteArray, private val ttlSeconds: Long = 3600) {

    // ───────────── State ─────────────

    /** Id for the next issued token: 1, 2, 3, … (lets you revoke one token without storing every token). */
    private var nextTokenId = 1L

    /** Token ids that were revoked. */
    private val revoked = HashSet<Long>()

    // ───────────── API ─────────────

    /**
     * Step 1 — create a token for [userId].
     * - [userId] blank or containing `|` → `IllegalArgumentException`.
     * - payload = `"<userId>|<now + ttlSeconds>|<tokenId>"` (tokenId = `nextTokenId`, then increase it).
     * - token = `b64(payload bytes) + "." + b64(hmacSha256(secret, payload))`.
     */
    fun issue(userId: String, now: Long): String {
        TODO("Step 1")
    }

    /**
     * Step 2 — turn a token back into [TokenData], or `null` if it's not genuine. Must never throw.
     * - Exactly 2 parts separated by `.`; both must base64-decode (use `unb64`).
     * - The signature must equal `hmacSha256(secret, payload)` — compare with `java.security.MessageDigest.isEqual`.
     * - The payload must have exactly 3 `|`-separated fields, and the last two must be numbers.
     */
    private fun decode(token: String): TokenData? {
        TODO("Step 2")
    }

    /** Step 3 — the user id if the token decodes, `now < expiresAt`, and its id isn't revoked; otherwise `null`. Never throws. */
    fun verify(token: String, now: Long): String? {
        TODO("Step 3")
    }

    /** Step 4 — revoke a genuine token (remember its id) and return `true`; a token that doesn't decode → `false`. */
    fun revoke(token: String): Boolean {
        TODO("Step 4")
    }
}
