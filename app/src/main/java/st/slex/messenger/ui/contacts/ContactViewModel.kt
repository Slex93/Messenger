package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.core.Mapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.ui.core.UIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val repository: ContactsRepository,
    private val mapper: Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactUIModel>>>
) : ViewModel() {

}