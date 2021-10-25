package st.slex.messenger.data.contacts

import st.slex.messenger.auth.core.Mapper
import st.slex.messenger.auth.core.Resource
import st.slex.messenger.ui.contacts.ContactUI

class ContactsDataMapper : Mapper.ToUI<List<ContactData>, Resource<List<ContactUI>>> {

    override fun map(data: List<ContactData>): Resource<List<ContactUI>> =
        Resource.Success(data.map {
            ContactUI.Base(
                id = it.getId,
                full_name = it.getFullName,
                phone = it.getPhone
            )
        })

    override fun map(exception: Exception): Resource<List<ContactUI>> = Resource.Failure(exception)

    override fun map(): Resource<List<ContactUI>> = Resource.Loading
}