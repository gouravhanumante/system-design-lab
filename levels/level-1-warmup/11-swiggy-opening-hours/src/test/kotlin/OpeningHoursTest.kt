import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OpeningHoursTest {

    @Test
    fun simpleIntervalIsHalfOpen() {
        val h = OpeningHours()
        h.addInterval(0, "11:00", "15:00")
        assertFalse(h.isOpen(0, "10:59"))
        assertTrue(h.isOpen(0, "11:00"))
        assertTrue(h.isOpen(0, "14:59"))
        assertFalse(h.isOpen(0, "15:00"))
        assertFalse(h.isOpen(1, "12:00"))
    }

    @Test
    fun overnightIntervalContinuesNextDay() {
        val h = OpeningHours()
        h.addInterval(4, "22:00", "02:00")
        assertTrue(h.isOpen(4, "23:59"))
        assertTrue(h.isOpen(5, "00:00"))
        assertTrue(h.isOpen(5, "01:59"))
        assertFalse(h.isOpen(5, "02:00"))
        assertFalse(h.isOpen(4, "01:00"))                 // Friday early morning: not this interval
    }

    @Test
    fun sundayNightWrapsIntoMonday() {
        val h = OpeningHours()
        h.addInterval(6, "23:00", "01:00")
        assertTrue(h.isOpen(6, "23:30"))
        assertTrue(h.isOpen(0, "00:30"))
        assertFalse(h.isOpen(0, "01:00"))
    }

    @Test
    fun severalIntervalsPerDay() {
        val h = OpeningHours()
        h.addInterval(2, "12:00", "15:00")
        h.addInterval(2, "19:00", "23:00")
        assertFalse(h.isOpen(2, "16:00"))
        assertTrue(h.isOpen(2, "20:00"))
    }

    @Test
    fun nextOpening() {
        val h = OpeningHours()
        h.addInterval(2, "12:00", "15:00")
        h.addInterval(2, "19:00", "23:00")
        h.addInterval(4, "22:00", "02:00")
        assertEquals(2 to "19:00", h.nextOpening(2, "16:00"))
        assertEquals(2 to "13:00", h.nextOpening(2, "13:00"))           // open now
        assertEquals(4 to "22:00", h.nextOpening(2, "23:00"))
        assertEquals(2 to "12:00", h.nextOpening(5, "02:00"))           // Saturday -> next Wednesday
        val fridayOnly = OpeningHours()
        fridayOnly.addInterval(4, "22:00", "02:00")
        assertEquals(4 to "22:00", fridayOnly.nextOpening(5, "02:00"))  // wraps almost a whole week
        assertEquals(2 to "12:00", h.nextOpening(6, "10:00"))           // over Sunday into next Wednesday
        assertEquals(5 to "01:00", h.nextOpening(5, "01:00"))           // inside Friday's overnight interval
    }

    @Test
    fun noIntervalsMeansNeverOpen() {
        val h = OpeningHours()
        assertFalse(h.isOpen(3, "12:00"))
        assertNull(h.nextOpening(3, "12:00"))
    }

    @Test
    fun invalidInput() {
        val h = OpeningHours()
        assertFailsWith<IllegalArgumentException> { h.addInterval(7, "10:00", "11:00") }
        assertFailsWith<IllegalArgumentException> { h.addInterval(0, "24:00", "11:00") }
        assertFailsWith<IllegalArgumentException> { h.addInterval(0, "10:60", "11:00") }
        assertFailsWith<IllegalArgumentException> { h.addInterval(0, "9:00", "11:00") }
        assertFailsWith<IllegalArgumentException> { h.addInterval(0, "10:00", "10:00") }
        assertFailsWith<IllegalArgumentException> { h.isOpen(-1, "10:00") }
        assertFailsWith<IllegalArgumentException> { h.isOpen(0, "ab:cd") }
    }
}
