package st.slex.messenger.data.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.contacts.ContactUI
import javax.inject.Inject

class ContactDataMapper @Inject constructor() : Mapper.ToUI<ContactData, Resource<ContactUI>> {

    override fun map(data: ContactData): Resource<ContactUI> =
        Resource.Success(with(data) {
            ContactUI.Base(
                id = getId,
                full_name = getFullName,
                phone = getPhone
            )
        })

    override fun map(exception: Exception): Resource<ContactUI> = Resource.Failure(exception)

    override fun map(): Resource<ContactUI> = Resource.Loading
}