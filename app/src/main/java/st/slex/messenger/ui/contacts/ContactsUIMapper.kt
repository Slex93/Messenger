package st.slex.messenger.ui.contacts

import st.slex.messenger.auth.core.Mapper
import st.slex.messenger.data.contacts.ContactData
import javax.inject.Inject

class ContactsUIMapper @Inject constructor() :
    Mapper.Data<List<ContactUI>, List<ContactData>> {

    override fun map(data: List<ContactUI>): List<ContactData> = data.map {
        ContactData.Base(
            id = it.getId,
            phone = it.getPhone,
            full_name = it.getFullName
        )
    }
}