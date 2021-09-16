package st.slex.messenger.ui.contacts

sealed class ContactsUIResult {
    data class Success(val data: List<ContactsUI>) : ContactsUIResult()
    data class Failure(val exception: Exception) : ContactsUIResult()
    object Loading : ContactsUIResult()
}
