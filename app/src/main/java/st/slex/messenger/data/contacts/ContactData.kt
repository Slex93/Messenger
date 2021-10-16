package st.slex.messenger.data.contacts

interface ContactData {

    val getId: String
    val getPhone: String
    val getFullName: String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
    ) : ContactData {

        override val getId: String
            get() = id
        override val getPhone: String
            get() = phone
        override val getFullName: String
            get() = full_name
    }
}