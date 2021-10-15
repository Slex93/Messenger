package st.slex.messenger.data.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.core.UIResult

class ContactsDataMapper : Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactsUI>>> {

    override fun map(data: List<ContactsData>): UIResult<List<ContactsUI>> =
        UIResult.Success(data.map {
            ContactsUI.Base(
                id = it.id(),
                full_name = it.fullName(),
                phone = it.phone()
            )
        })

    override fun map(exception: Exception): UIResult<List<ContactsUI>> =
        UIResult.Failure(exception = exception)

}