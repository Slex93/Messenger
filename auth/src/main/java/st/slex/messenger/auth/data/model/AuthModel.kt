package st.slex.messenger.auth.data.model

interface AuthModel {

    fun id(): String
    fun phone(): String

    data class Base(
        private val id: String,
        private val phone: String
    ) : AuthModel {

        override fun id(): String = id
        override fun phone(): String = phone
    }
}