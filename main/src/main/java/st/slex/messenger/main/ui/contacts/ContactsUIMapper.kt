package st.slex.messenger.main.ui.contacts

import st.slex.core.Mapper
import st.slex.messenger.main.data.contacts.ContactData
import javax.inject.Inject

interface ContactsUIMapper : Mapper.Data<List<ContactUI>, List<ContactData>> {

    class Base @Inject constructor() : ContactsUIMapper {

        override fun map(data: List<ContactUI>): List<ContactData> = data.map {
            ContactData.Base(
                id = it.getId,
                phone = it.getPhone,
                full_name = it.getFullName
            )
        }
    }
}