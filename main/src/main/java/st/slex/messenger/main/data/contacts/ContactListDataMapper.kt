package st.slex.messenger.main.data.contacts

import st.slex.core.Mapper
import st.slex.core.Resource
import st.slex.messenger.main.ui.contacts.ContactUI
import javax.inject.Inject

interface ContactListDataMapper : Mapper.ToUI<List<ContactData>, Resource<List<ContactUI>>> {

    class Base @Inject constructor() : ContactListDataMapper {

        override fun map(data: List<ContactData>): Resource<List<ContactUI>> =
            Resource.Success(data.map {
                ContactUI.Base(
                    id = it.getId,
                    full_name = it.getFullName,
                    phone = it.getPhone
                )
            })

        override fun map(exception: Exception): Resource<List<ContactUI>> =
            Resource.Failure(exception)

        override fun map(): Resource<List<ContactUI>> = Resource.Loading
    }
}