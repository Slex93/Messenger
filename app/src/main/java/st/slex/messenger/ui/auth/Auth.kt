package st.slex.messenger.ui.auth

import st.slex.messenger.data.UserInitial

interface Auth {
    fun <T> map(mapper: AuthResultMapper<T>): T

    data class Base(private val profile: Map<String, Any>) : Auth {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(profile)
    }

    data class Fail(val e: Exception) : Auth {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(emptyMap())
    }

    interface AuthResultMapper<T> {
        fun map(profile: Map<String, Any>): T

        class Base : AuthResultMapper<UserInitial> {

            override fun map(profile: Map<String, Any>) = UserInitial(
                profile["name"].makeString(),
                profile["username"].makeString().lowercase(),
                profile["phone"].makeString(),
                profile["bio"].makeString(),
                profile["avatar_url"].makeString()
            )
        }
    }
}

fun Any?.makeString(): String {
    val toString = toString()
    return if (toString == "null") "" else toString
}