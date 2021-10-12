package st.slex.messenger.core.model.firebase

data class FirebaseUserModel(
    val id: String? = "",
    val username: String? = "",
    val url: String? = "",
    val state: String? = "",
    val contacts: List<FirebaseContactModel>?
)
