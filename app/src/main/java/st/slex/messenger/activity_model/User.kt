package st.slex.messenger.activity_model

data class User(
    val id: String = "",
    val phone: String = "",
    val username: String = "",
    val url: String = "",
    val bio: String = "",
    val fullname: String = "",
    val state: String = "",
)

data class Contact(
    val id: String = "",
    val phone: String  = "",
    val fullname: String = "",
)
