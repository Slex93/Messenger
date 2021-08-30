package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.ValueEventResponse
import javax.inject.Inject

class SingleChatRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : SingleChatRepository {

    override suspend fun getStatus(uid: String): Flow<ValueEventResponse> = service.valueEventFlow(
        databaseReference = databaseReference.child(NODE_USER).child(uid).child(CHILD_STATE)
    )

    override suspend fun getMessages(limitToLast: Int): Flow<ChildEventResponse> =
        service.childEventFlow(
            databaseReference = databaseReference.child(NODE_MESSAGE).child(auth.uid.toString()),
            limitToLast = limitToLast
        )

    override suspend fun sendMessage(message: String, uid: String): Unit =
        withContext(Dispatchers.IO) {
            val refDialogUser = "$NODE_MESSAGE/${auth.currentUser?.uid}/$uid"
            val refDialogReceivingUser = "$NODE_MESSAGE/$uid/${auth.currentUser?.uid}"
            val messageKey = databaseReference.child(refDialogUser).push().key
            val mapMessage = hashMapOf<String, Any>()
            mapMessage[CHILD_FROM] = auth.currentUser?.uid.toString()
            mapMessage[CHILD_TEXT] = message
            mapMessage[CHILD_ID] = messageKey.toString()
            mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
            val mapDialog = hashMapOf<String, Any>()
            mapDialog["$refDialogUser/$messageKey"] = mapMessage
            mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage
            databaseReference
                .updateChildren(mapDialog)
                .addOnSuccessListener {
                    setInMainList(uid)
                }
        }

    private fun setInMainList(uid: String) {
        databaseReference.child(NODE_MAIN_LIST).child(auth.currentUser?.uid.toString()).child(uid)
            .addListenerForSingleValueEvent(AppValueEventListener {
                var refUser = "$NODE_MAIN_LIST/${auth.currentUser?.uid}/$uid"
                var refReceived = "$NODE_MAIN_LIST/$uid/${auth.currentUser?.uid}"
                val mapUser = hashMapOf<String, Any>()
                val mapReceived = hashMapOf<String, Any>()
                val commonMap = hashMapOf<String, Any>()
                mapUser[CHILD_ID] = uid
                mapReceived[CHILD_ID] = auth.currentUser?.uid.toString()
                commonMap[refUser] = mapUser
                commonMap[refReceived] = mapReceived
                databaseReference.updateChildren(commonMap)
            })
    }

}