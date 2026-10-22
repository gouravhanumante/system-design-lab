class VersionedFile(private val maxVersions: Int = 5) {

    fun save(content: String): Int {
        TODO()
    }

    fun current(): String? {
        TODO()
    }

    fun versions(): List<Int> {
        TODO()
    }

    fun read(version: Int): String {
        TODO()
    }

    fun restore(version: Int): Int {
        TODO()
    }
}
