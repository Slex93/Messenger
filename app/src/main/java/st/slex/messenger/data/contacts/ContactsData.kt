package st.slex.messenger.data.contacts

interface ContactsData {

    fun id(): String
    fun phone(): String
    fun fullName(): String
    fun url(): String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
        val url: String = ""
    ) : ContactsData {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun fullName(): String = full_name
        override fun url(): String = url
    }
}