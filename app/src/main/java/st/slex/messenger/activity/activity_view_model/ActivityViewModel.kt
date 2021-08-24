package st.slex.messenger.activity.activity_view_model

import androidx.lifecycle.ViewModel
import st.slex.common.messenger.activity.activity_model.ActivityRepository
import st.slex.common.messenger.activity.activity_model.Contact

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {

    val getUserForHeader = repository.getUserForHeader

    fun initFirebase() {
        repository.initFirebase()
    }

    fun signOut() {
        repository.signOut()
    }

    fun initUser() {
        repository.initUser()
    }

    fun updatePhoneToDatabase(listContacts: List<Contact>) {
        repository.updatePhonesToDatabase(listContacts)
    }

    fun statusOnline(){
        repository.statusOnline()
    }

    fun statusOffline(){
        repository.statusOffline()
    }

}