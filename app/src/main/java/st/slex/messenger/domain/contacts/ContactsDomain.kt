package st.slex.messenger.domain.contacts

interface ContactsDomain {

    fun id(): String
    fun phone(): String
    fun fullName(): String
    fun url(): String

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val full_name: String = "",
        private val url: String = ""
    ) : ContactsDomain {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun fullName(): String = full_name
        override fun url(): String = url
    }
}