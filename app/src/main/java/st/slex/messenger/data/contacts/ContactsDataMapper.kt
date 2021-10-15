package st.slex.messenger.data.contacts

import st.slex.messenger.core.Mapper
import st.slex.messenger.ui.contacts.ContactUIModel
import st.slex.messenger.ui.core.UIResult

class ContactsDataMapper : Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactUIModel>>> {

    override fun map(data: List<ContactsData>): UIResult<List<ContactUIModel>> =
        UIResult.Success(data.map {
            ContactUIModel.Base(
                id = it.id(),
                phone = it.phone(),
                full_name = it.fullName(),
                url = it.url(),
            )
        })

    override fun map(exception: Exception): UIResult<List<ContactUIModel>> =
        UIResult.Failure(exception)

}