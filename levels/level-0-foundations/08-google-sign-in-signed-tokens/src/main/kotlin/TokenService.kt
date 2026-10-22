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

class TokenService(private val secret: ByteArray, private val ttlSeconds: Long = 3600) {

    fun issue(userId: String, now: Long): String {
        TODO()
    }

    /** The user id if the token is genuine, unexpired and not revoked; otherwise null. Never throws. */
    fun verify(token: String, now: Long): String? {
        TODO()
    }

    fun revoke(token: String): Boolean {
        TODO()
    }
}
