/** The 62 "digits", in order: value 0 is '0', value 10 is 'a', value 36 is 'A', value 61 is 'Z'. */
private const val ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

/**
 * Step 1 — write [n] in base 62 using [ALPHABET] (like writing a number in base 10, but with 62 digits).
 * - `0` → `"0"`, `61` → `"Z"`, `62` → `"10"`.
 * - Negative [n] → throw `IllegalArgumentException` (`require(...)` does this).
 */
fun encodeBase62(n: Long): String {
    TODO("Step 1")
}

/**
 * Step 2 — the reverse of [encodeBase62]: `"10"` → `62`.
 * - Empty string, or a character that isn't in [ALPHABET] → `IllegalArgumentException`.
 */
fun decodeBase62(s: String): Long {
    TODO("Step 2")
}

class UrlShortener {

    // ───────────── State ─────────────

    /** short code (or custom alias) → long URL. This is what `resolve` reads. */
    private val codeToUrl = HashMap<String, String>()

    /** long URL → its generated code, so shortening the same URL twice returns the same code. (Aliases are NOT stored here.) */
    private val urlToCode = HashMap<String, String>()

    /** short code → how many times it was resolved. */
    private val clickCount = HashMap<String, Int>()

    /** Next number to turn into a code with `encodeBase62` (start at 1). */
    private var counter = 1L

    // ───────────── Helper ─────────────

    /** Step 3a — a URL is valid only if it starts with `http://` or `https://`. */
    private fun isValidUrl(url: String): Boolean {
        TODO("Step 3a")
    }

    // ───────────── Public API ─────────────

    /**
     * Step 3b — give back a short code for [longUrl].
     * - Invalid URL → `IllegalArgumentException`.
     * - Already shortened before → return the same code.
     * - Otherwise: code = `encodeBase62(counter)`, then `counter++`. If that code is already taken
     *   (someone made it a custom alias), skip to the next number until you find a free one.
     *   Save it in `codeToUrl` and `urlToCode`.
     */
    fun shorten(longUrl: String): String {
        TODO("Step 3b")
    }

    /**
     * Step 4 — let the user choose the code ([alias]) for [longUrl].
     * - [alias] must be 3–30 characters of letters, digits or `-`, and the URL must be valid → otherwise `IllegalArgumentException`.
     * - Alias already used (as an alias OR a generated code) → return `false` and change nothing.
     * - Otherwise save it in `codeToUrl` only (not in `urlToCode`) and return `true`.
     */
    fun customAlias(alias: String, longUrl: String): Boolean {
        TODO("Step 4")
    }

    /**
     * Step 3c + 5 — the long URL for [code], or `null` if unknown.
     * Step 5: every successful resolve adds 1 to that code's click count.
     */
    fun resolve(code: String): String? {
        TODO("Step 3c")
    }

    /** Step 5 — how many times [code] was resolved (0 if never or unknown). */
    fun clicks(code: String): Int {
        TODO("Step 5")
    }
}
