package st.slex.messenger.main.data.contacts

interface ContactData {

    fun copy(
        id: String? = null,
        phone: String? = null,
        full_name: String? = null
    ): ContactData

    val getId: String
    val getPhone: String
    val getFullName: String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
    ) : ContactData {

        override fun copy(
            id: String?,
            phone: String?,
            full_name: String?
        ): ContactData = Base(
            id = id ?: this.id,
            phone = phone ?: this.phone,
            full_name = full_name ?: this.full_name
        )

        override val getId: String
            get() = id
        override val getPhone: String
            get() = phone
        override val getFullName: String
            get() = full_name
    }
}