class UsersTable {

    // ───────────── State ─────────────

    /** The table itself: id → (age, city). A TreeMap keeps rows sorted by id. */
    private val rows = java.util.TreeMap<Int, Pair<Int, String>>()

    /** Sorted index on age (null until `createIndex("age")`): age → ids with that age. Sorted keys make range queries cheap. */
    private var ageIndex: java.util.TreeMap<Int, MutableSet<Int>>? = null

    /** Hash index on city (null until `createIndex("city")`): city → ids in that city. */
    private var cityIndex: HashMap<String, MutableSet<Int>>? = null

    /** How many rows the last `findBy…` call had to look at. The tests use this to check you really used the index. */
    var lastRowsExamined: Int = 0
        private set

    // ───────────── Writes ─────────────

    /** Step 1/5 — add a row. Id already exists → `IllegalArgumentException`. Step 5: also add it to every index that exists. */
    fun insert(id: Int, age: Int, city: String) {
        TODO("Step 1")
    }

    /** Step 1/5 — remove the row (unknown id: do nothing). Step 5: also remove its id from every index that exists. */
    fun delete(id: Int) {
        TODO("Step 1")
    }

    /** Step 5 helper — put one row's id into whichever indexes exist (use it from `insert` and `createIndex`). */
    private fun addToIndexes(id: Int, age: Int, city: String) {
        TODO("Step 5")
    }

    /**
     * Steps 3–4 — build an index over the rows that already exist.
     * - `"age"` → fill `ageIndex`; `"city"` → fill `cityIndex`. Creating an index that already exists does nothing.
     * - Any other column → `IllegalArgumentException`.
     */
    fun createIndex(column: String) {
        TODO("Step 3")
    }

    // ───────────── Reads ─────────────

    /**
     * Ids with minAge <= age <= maxAge, sorted by id.
     * - Step 2, no index: look at every row → `lastRowsExamined = rows.size`.
     * - Step 3, with `ageIndex`: only read the ages in the range (TreeMap `subMap`) → `lastRowsExamined` = number of ids found.
     *   `minAge > maxAge` → empty list.
     */
    fun findByAge(minAge: Int, maxAge: Int): List<Int> {
        TODO("Step 2")
    }

    /**
     * Ids in that city, sorted by id.
     * - Step 2, no index: full scan → `lastRowsExamined = rows.size`.
     * - Step 4, with `cityIndex`: one lookup → `lastRowsExamined` = number of ids found.
     */
    fun findByCity(city: String): List<Int> {
        TODO("Step 2")
    }
}
