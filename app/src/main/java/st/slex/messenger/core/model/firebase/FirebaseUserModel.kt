package st.slex.messenger.core.model.firebase

import st.slex.messenger.data.contacts.FirebaseContactModel

data class FirebaseUserModel(
    val id: String? = "",
    val username: String? = "",
    val url: String? = "",
    val state: String? = "",
    val contacts: List<FirebaseContactModel>?
)
