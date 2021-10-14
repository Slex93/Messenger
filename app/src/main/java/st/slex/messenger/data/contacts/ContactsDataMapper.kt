package st.slex.messenger.data.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.core.UIResult

class ContactsDataMapper : Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactsUI>>> {

    override fun map(data: List<ContactsData>): UIResult<List<ContactsUI>> =
        UIResult.Success(data.map {
            ContactsUI.Base(
                id = it.id(),
                phone = it.phone(),
                full_name = it.fullName(),
                url = it.url(),
            )
        })

    override fun map(exception: Exception): UIResult<List<ContactsUI>> = UIResult.Failure(exception)

}