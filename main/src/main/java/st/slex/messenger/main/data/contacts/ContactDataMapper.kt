package st.slex.messenger.main.data.contacts

import st.slex.core.Mapper
import st.slex.core.Resource
import st.slex.messenger.main.ui.contacts.ContactUI
import javax.inject.Inject

interface ContactDataMapper : Mapper.ToUI<ContactData, Resource<ContactUI>> {

    class Base @Inject constructor() : ContactDataMapper {

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
}