package st.slex.messenger.domain.auth

interface LoginDomain {

    fun id(): String
    fun phone(): String

    data class Base(
        val id: String,
        val phone: String
    ) : LoginDomain {

        override fun id(): String = id
        override fun phone(): String = phone
    }
}