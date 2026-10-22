import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IssuesApiTest {
    private val api = IssuesApi()

    private fun create(title: String) = api.handle(Request("POST", "/issues", mapOf("title" to title)))

    @Test
    fun createReturns201WithTheNewIssue() {
        val r = create("Crash on login")
        assertEquals(201, r.status)
        assertEquals(mapOf("id" to "1", "title" to "Crash on login", "state" to "open"), r.body)
        assertEquals("2", create("Second").body["id"])
    }

    @Test
    fun createValidatesTheTitle() {
        val r = api.handle(Request("POST", "/issues"))
        assertEquals(400, r.status)
        assertTrue("error" in r.body)
        assertEquals(400, create("   ").status)
        assertEquals(0, api.handle(Request("GET", "/issues")).items.size)
    }

    @Test
    fun getOneAndNotFound() {
        create("A")
        assertEquals(200, api.handle(Request("GET", "/issues/1")).status)
        assertEquals("A", api.handle(Request("GET", "/issues/1")).body["title"])
        val missing = api.handle(Request("GET", "/issues/99"))
        assertEquals(404, missing.status)
        assertTrue("error" in missing.body)
        assertEquals(404, api.handle(Request("GET", "/issues/abc")).status)
        assertEquals(404, api.handle(Request("GET", "/issues/0")).status)
    }

    @Test
    fun listIsSortedAndFilterable() {
        create("A"); create("B"); create("C")
        api.handle(Request("PATCH", "/issues/2", mapOf("state" to "closed")))
        assertEquals(listOf("1", "2", "3"), api.handle(Request("GET", "/issues")).items.map { it["id"] })
        assertEquals(listOf("1", "3"), api.handle(Request("GET", "/issues", query = mapOf("state" to "open"))).items.map { it["id"] })
        assertEquals(listOf("2"), api.handle(Request("GET", "/issues", query = mapOf("state" to "closed"))).items.map { it["id"] })
        assertEquals(400, api.handle(Request("GET", "/issues", query = mapOf("state" to "weird"))).status)
    }

    @Test
    fun patchUpdatesOnlyGivenFieldsAndValidatesFirst() {
        create("Old title")
        val r = api.handle(Request("PATCH", "/issues/1", mapOf("title" to "New title")))
        assertEquals(200, r.status)
        assertEquals(mapOf("id" to "1", "title" to "New title", "state" to "open"), r.body)
        val bad = api.handle(Request("PATCH", "/issues/1", mapOf("title" to "Sneaky", "state" to "deleted")))
        assertEquals(400, bad.status)
        assertEquals("New title", api.handle(Request("GET", "/issues/1")).body["title"])   // nothing changed
        assertEquals(400, api.handle(Request("PATCH", "/issues/1", mapOf("title" to ""))).status)
        assertEquals(404, api.handle(Request("PATCH", "/issues/5", mapOf("state" to "closed"))).status)
    }

    @Test
    fun deleteIsIdempotentInEffect() {
        create("A")
        val first = api.handle(Request("DELETE", "/issues/1"))
        assertEquals(204, first.status)
        assertEquals(emptyMap(), first.body)
        assertEquals(404, api.handle(Request("DELETE", "/issues/1")).status)
        assertEquals(404, api.handle(Request("GET", "/issues/1")).status)
        assertEquals("2", create("B").body["id"])          // ids are never reused
    }

    @Test
    fun unknownPathsAndMethods() {
        assertEquals(404, api.handle(Request("GET", "/pulls")).status)
        assertEquals(404, api.handle(Request("GET", "/issues/1/comments")).status)
        assertEquals(405, api.handle(Request("PUT", "/issues")).status)
        assertEquals(405, api.handle(Request("DELETE", "/issues")).status)
        create("A")
        assertEquals(405, api.handle(Request("POST", "/issues/1")).status)
    }
}
