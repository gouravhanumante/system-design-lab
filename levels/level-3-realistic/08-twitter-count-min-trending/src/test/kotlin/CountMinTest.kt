import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CountMinTest {

    @Test
    fun exactWhenThereIsRoomForEveryone() {
        val s = CountMinSketch(10_000, 4)
        repeat(7) { s.add("#kotlin") }
        s.add("#android", 3)
        assertEquals(7L, s.estimate("#kotlin"))
        assertEquals(3L, s.estimate("#android"))
        assertEquals(0L, s.estimate("#never"))
    }

    @Test
    fun neverUnderestimates() {
        val s = CountMinSketch(16, 3)
        val truth = HashMap<String, Long>()
        val rnd = Random(1)
        repeat(5_000) {
            val tag = "#t${rnd.nextInt(300)}"
            s.add(tag); truth.merge(tag, 1L, Long::plus)
        }
        for ((tag, n) in truth) assertTrue(s.estimate(tag) >= n, "$tag: ${s.estimate(tag)} < $n")
    }

    @Test
    fun collisionsInflateButMinimumHelps() {
        val one = CountMinSketch(8, 1)
        val four = CountMinSketch(8, 4)
        val rnd = Random(9)
        repeat(4_000) {
            val tag = "#t${rnd.nextInt(200)}"
            one.add(tag); four.add(tag)
        }
        var errOne = 0L; var errFour = 0L
        for (i in 0 until 200) {
            errOne += one.estimate("#t$i"); errFour += four.estimate("#t$i")
        }
        assertTrue(errFour <= errOne, "more rows should not make estimates worse")
    }

    @Test
    fun invalidSizes() {
        assertFailsWith<IllegalArgumentException> { CountMinSketch(0, 3) }
        assertFailsWith<IllegalArgumentException> { CountMinSketch(10, 0) }
    }

    @Test
    fun trendingFindsTheHeavyHitters() {
        val t = TrendingTags(3)
        val rnd = Random(3)
        val stream = ArrayList<String>()
        repeat(500) { stream.add("#worldcup") }
        repeat(300) { stream.add("#election") }
        repeat(200) { stream.add("#iphone") }
        repeat(1_000) { stream.add("#noise${rnd.nextInt(500)}") }
        stream.shuffle(rnd)
        stream.forEach { t.observe(it) }
        assertEquals(listOf("#worldcup" to 500L, "#election" to 300L, "#iphone" to 200L), t.top())
    }

    @Test
    fun replacesOnlyWhenStrictlyGreaterAndEvictsTheWeakest() {
        val t = TrendingTags(2, width = 10_000)
        t.observe("#a"); t.observe("#a")
        t.observe("#b")
        t.observe("#c")                          // estimate 1, not > 1: stays out
        assertEquals(listOf("#a" to 2L, "#b" to 1L), t.top())
        t.observe("#c")                          // estimate 2 > 1: replaces #b
        assertEquals(listOf("#a" to 2L, "#c" to 2L), t.top())
    }

    @Test
    fun tieForWeakestEvictsAlphabeticallyLargest() {
        val t = TrendingTags(2, width = 10_000)
        t.observe("#x"); t.observe("#m")
        t.observe("#q"); t.observe("#q")         // both candidates have 1: #x (largest) goes
        assertEquals(listOf("#q" to 2L, "#m" to 1L), t.top())
    }

    @Test
    fun topReportsCurrentEstimates() {
        val t = TrendingTags(2, width = 10_000)
        t.observe("#a"); t.observe("#b")
        repeat(5) { t.observe("#b") }
        assertEquals(listOf("#b" to 6L, "#a" to 1L), t.top())
    }
}
