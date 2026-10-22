enum class Permission { NONE, VIEW, EDIT, OWNER }

class SharedDrive {

    fun addToGroup(group: String, user: String) {
        TODO()
    }

    fun createFolder(path: String, creator: String) {
        TODO()
    }

    fun share(path: String, principal: String, permission: Permission, by: String) {
        TODO()
    }

    fun revoke(path: String, principal: String, by: String) {
        TODO()
    }

    fun breakInheritance(path: String, by: String) {
        TODO()
    }

    fun permissionOf(user: String, path: String): Permission {
        TODO()
    }
}
