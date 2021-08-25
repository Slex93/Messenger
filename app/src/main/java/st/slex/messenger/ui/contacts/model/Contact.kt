package st.slex.messenger.ui.contacts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: String = "",
    val phone: String = "",
    val fullname: String = "",
    val url: String = ""
) : Parcelable
