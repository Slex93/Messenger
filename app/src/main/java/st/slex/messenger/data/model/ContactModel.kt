package st.slex.messenger.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactModel(
    val id: String = "",
    val phone: String = "",
    val fullname: String = "",
    val url: String = ""
) : Parcelable