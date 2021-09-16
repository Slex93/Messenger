package st.slex.messenger.data.contacts

import st.slex.messenger.core.Abstract
import st.slex.messenger.domain.contacts.ContactsDomain
import st.slex.messenger.domain.contacts.ContactsDomainResult

interface ContactsDataMapper<T> : Abstract.Mapper.DataToDomain<List<ContactsData>, T> {

    class Base : ContactsDataMapper<ContactsDomainResult> {
        override fun map(data: List<ContactsData>): ContactsDomainResult =
            ContactsDomainResult.Success(data.map {
                ContactsDomain.Base(
                    id = it.id(),
                    phone = it.phone(),
                    full_name = it.fullName(),
                    url = it.url(),
                )
            })

        override fun map(exception: Exception): ContactsDomainResult {
            return ContactsDomainResult.Failure(exception)
        }
    }
}