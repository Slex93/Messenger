package st.slex.messenger.data.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.contacts.ContactsUI

class ContactsDataMapper : Mapper.ToUI<List<ContactsData>, Resource<List<ContactsUI>>> {

    override fun map(data: List<ContactsData>): Resource<List<ContactsUI>> =
        Resource.Success(data.map {
            ContactsUI.Base(
                id = it.getId,
                full_name = it.getFullName,
                phone = it.getPhone
            )
        })

    override fun map(exception: Exception): Resource<List<ContactsUI>> = Resource.Failure(exception)

    override fun map(): Resource<List<ContactsUI>> = Resource.Loading
}