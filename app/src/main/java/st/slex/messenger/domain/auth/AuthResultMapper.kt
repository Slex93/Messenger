package st.slex.messenger.domain.auth

interface AuthResultMapper<T> {
    fun map(profile: Map<String, Any>): T
    fun map(exception: Exception)
    class Base : AuthResultMapper<UserInitial> {

        override fun map(profile: Map<String, Any>) = UserInitial(
            profile["uid"].makeString(),
            profile["phone"].makeString()
        )

        private fun Any?.makeString(): String {
            val toString = toString()
            return if (toString == "null") "" else toString
        }

        override fun map(exception: Exception) {
            TODO("Not yet implemented")
        }
    }
}