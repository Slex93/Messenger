package st.slex.messenger.auth.data

interface AuthData {

    fun id(): String
    fun phone(): String

    data class Base(
        private val id: String,
        private val phone: String
    ) : AuthData {

        override fun id(): String = id
        override fun phone(): String = phone
    }
}