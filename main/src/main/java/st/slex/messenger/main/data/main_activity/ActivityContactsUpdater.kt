package st.slex.messenger.main.data.main_activity

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import st.slex.messenger.core.FirebaseConstants.CHILD_FULL_NAME
import st.slex.messenger.core.FirebaseConstants.CHILD_ID
import st.slex.messenger.core.FirebaseConstants.CHILD_PHONE
import st.slex.messenger.core.FirebaseConstants.NODE_CONTACT
import st.slex.messenger.core.FirebaseConstants.NODE_USER
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.contacts.ContactData
import st.slex.messenger.main.data.core.ValueListener
import st.slex.messenger.main.utilites.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ActivityContactsUpdater {

    suspend fun listener(
        contactsList: List<ContactData>,
        eventResult: (Resource<Nothing?>) -> Unit
    ): ValueEventListener

    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser
    ) : ActivityContactsUpdater {

        override suspend fun listener(
            contactsList: List<ContactData>,
            eventResult: (Resource<Nothing?>) -> Unit
        ): ValueEventListener = withContext(Dispatchers.IO) {
            return@withContext ValueListener(
                cancelled = { result -> eventResult(result) },
                dataChange = { snapshot ->
                    val filteredContacts = filterContactsBySnapshot(contactsList, snapshot)
                    filteredContacts.forEach { contact ->
                        val task = contactsReference
                            .child(contact.getId)
                            .setValue(mapContact(contact))
                        this@withContext.launch(Dispatchers.IO) {
                            eventResult(handle(task))
                        }
                    }
                }
            )
        }

        private suspend fun handle(result: Task<Void>): Resource<Nothing?> =
            suspendCoroutine { continuation ->
                result.addOnSuccessListener { continuation.resume(Resource.Success(null)) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }

        private fun filterContactsBySnapshot(
            contacts: List<ContactData>,
            snapshot: DataSnapshot
        ): List<ContactData> {
            val mappedSnapshot = snapshot.mapIdToPhones()
            val filteredContacts = contacts.filterBySnapshots(mappedSnapshot)
            return filteredContacts.saveIdFromSnapMap(mappedSnapshot)
        }

        private fun List<ContactData>.filterBySnapshots(
            mappedSnapshot: Map<String, String>
        ): List<ContactData> = filter { contact ->
            mappedSnapshot.containsKey(contact.getPhone) && !mappedSnapshot.containsValue(auth.uid)
        }

        private fun List<ContactData>.saveIdFromSnapMap(
            mappedSnapshot: Map<String, String>
        ): List<ContactData> = map { contact ->
            contact.copy(id = mappedSnapshot[contact.getPhone])
        }

        private fun DataSnapshot.mapIdToPhones(): Map<String, String> =
            children.mapNotNull { it.key.toString() to it.value.toString() }.toMap()

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(auth.uid).child(NODE_CONTACT)
        }

        private fun mapContact(contact: ContactData) = mapOf<String, Any>(
            CHILD_ID to contact.getId,
            CHILD_PHONE to contact.getPhone,
            CHILD_FULL_NAME to contact.getFullName
        )
    }
}