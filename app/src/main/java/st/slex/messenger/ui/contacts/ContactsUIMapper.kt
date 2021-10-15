package st.slex.messenger.ui.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.data.contacts.ContactsData
import javax.inject.Inject

class ContactsUIMapper @Inject constructor() :
    Mapper.Data<List<ContactsUI>, List<ContactsData>> {

    override fun map(data: List<ContactsUI>): List<ContactsData> = data.map {
        ContactsData.Base(
            id = it.getId,
            phone = it.getPhone,
            full_name = it.getFullName
        )
    }
}