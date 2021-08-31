package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppChildEventListener
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleChatRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : SingleChatRepository {

    override suspend fun getUser(uid: String): Flow<Response<UserModel>> = callbackFlow {
        val reference = databaseReference.child(NODE_USER).child(uid)
        val callback = AppValueEventListener({ snapshot ->
            val user = snapshot.getThisValue<UserModel>()
            val referenceContact = databaseReference
                .child(NODE_CONTACT)
                .child(auth.uid.toString())
                .child(uid)
                .child(CHILD_FULL_NAME)
            val callbackContact = AppValueEventListener({
                trySendBlocking(Response.Success(user.copy(full_name = it.value.toString())))
            }, { exception ->
                trySendBlocking(Response.Failure(exception))
            })
            referenceContact.addListenerForSingleValueEvent(callbackContact)
        }, { exception ->
            trySendBlocking(Response.Failure(exception))
        })
        reference.addValueEventListener(callback)
        awaitClose { reference.removeEventListener(callback) }
    }

    override suspend fun getMessages(uid: String, limitToLast: Int): Flow<Response<MessageModel>> =
        callbackFlow {
            val reference = databaseReference.child(NODE_CHAT).child(auth.uid.toString()).child(uid)
                .limitToLast(limitToLast)
            val listener = AppChildEventListener({ snapshot ->
                trySendBlocking(Response.Success(snapshot.getThisValue()))
            }, { exception ->
                trySendBlocking(Response.Failure(exception))
            })
            reference.addChildEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

    override suspend fun sendMessage(message: String, uid: String): Unit =
        withContext(Dispatchers.IO) {
            val refDialogUser = "$NODE_CHAT/${auth.currentUser?.uid}/$uid"
            val refDialogReceivingUser = "$NODE_CHAT/$uid/${auth.currentUser?.uid}"
            val messageKey = databaseReference.child(refDialogUser).push().key
            val mapMessage = hashMapOf<String, Any>()
            mapMessage[CHILD_FROM] = auth.currentUser?.uid.toString()
            mapMessage[CHILD_TEXT] = message
            mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
            val mapDialog = hashMapOf<String, Any>()
            mapDialog["$refDialogUser/$messageKey"] = mapMessage
            mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage
            databaseReference
                .updateChildren(mapDialog)
                .addOnSuccessListener {
                    setInMainList(uid, messageKey.toString())
                }
        }

    private fun setInMainList(uid: String, messageKey: String) {
        val userReference =
            databaseReference.child(NODE_CHAT_LIST).child(auth.currentUser?.uid.toString())
                .child(uid)
        val receiverReference = databaseReference.child(NODE_CHAT_LIST).child(uid)
            .child(auth.currentUser?.uid.toString())

        userReference.setValue(messageKey)
        receiverReference.setValue(messageKey)


    }

}