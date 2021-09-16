package st.slex.messenger.domain.contacts

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.contacts.ContactsUIResult

interface ContactsDomainMapper<T> : Abstract.Mapper.DataToDomain<List<ContactsDomain>, T> {

    class Base : ContactsDomainMapper<ContactsUIResult> {
        override fun map(data: List<ContactsDomain>): ContactsUIResult =
            ContactsUIResult.Success(data.map {
                ContactsUI.Base(
                    id = it.id(),
                    phone = it.phone(),
                    full_name = it.fullName(),
                    url = it.url(),
                )
            })

        override fun map(exception: Exception): ContactsUIResult {
            return ContactsUIResult.Failure(exception)
        }
    }
}