package st.slex.messenger.domain.auth

import st.slex.messenger.data.auth.UserInitial

sealed class Auth {

    abstract fun <T> map(mapper: AuthResultMapper<T>): T

    data class Base(private val profile: Map<String, Any>) : Auth() {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(profile)
    }

    data class Failure(val e: Exception) : Auth() {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(emptyMap())
    }

    data class SendCode(private val id: String) : Auth() {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(emptyMap())
    }

    interface AuthResultMapper<T> {
        fun map(profile: Map<String, Any>): T

        class Base : AuthResultMapper<UserInitial> {

            override fun map(profile: Map<String, Any>) = UserInitial(
                profile["uid"].makeString(),
                profile["phone"].makeString()
            )

            private fun Any?.makeString(): String {
                val toString = toString()
                return if (toString == "null") "" else toString
            }
        }
    }
}
