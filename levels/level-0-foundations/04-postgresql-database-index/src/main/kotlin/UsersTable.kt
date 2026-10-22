class UsersTable {

    var lastRowsExamined: Int = 0
        private set

    fun insert(id: Int, age: Int, city: String) {
        TODO()
    }

    fun delete(id: Int) {
        TODO()
    }

    fun createIndex(column: String) {
        TODO()
    }

    /** Ids with minAge <= age <= maxAge, sorted by id. */
    fun findByAge(minAge: Int, maxAge: Int): List<Int> {
        TODO()
    }

    /** Ids in that city, sorted by id. */
    fun findByCity(city: String): List<Int> {
        TODO()
    }
}
