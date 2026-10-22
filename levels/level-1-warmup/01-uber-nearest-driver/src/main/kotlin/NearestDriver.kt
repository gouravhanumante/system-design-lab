data class Driver(val id: String, val x: Int, val y: Int)

class DriverFinder(private val drivers: List<Driver>) {

    /** Ids of the [k] drivers closest to (x, y), nearest first. Ties: smaller id first. */
    fun nearest(x: Int, y: Int, k: Int): List<String> {
        TODO("Return the ids of the k closest drivers")
    }
}
