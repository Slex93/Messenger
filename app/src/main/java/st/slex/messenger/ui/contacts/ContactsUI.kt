package st.slex.messenger.ui.contacts

interface ContactsUI {

    fun id(): String
    fun phone(): String
    fun fullName(): String

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val full_name: String = "",
    ) : ContactsUI {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun fullName(): String = full_name
    }
}