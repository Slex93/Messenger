package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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

    override suspend fun sendMessage(message: String, user: UserModel): Unit =
        withContext(Dispatchers.IO) {
            val refDialogUser = "$NODE_CHAT/${auth.currentUser?.uid}/${user.id}"
            val refDialogReceivingUser = "$NODE_CHAT/${user.id}/${auth.currentUser?.uid}"
            val messageKey = databaseReference.child(refDialogUser).push().key
            val mapMessage = hashMapOf<String, Any>()
            mapMessage[CHILD_FROM] = auth.currentUser?.uid.toString()
            mapMessage[CHILD_TEXT] = message
            mapMessage[CHILD_TIMESTAMP] = System.currentTimeMillis()
            val mapDialog = hashMapOf<String, Any>()
            mapDialog["$refDialogUser/$messageKey"] = mapMessage
            mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage
            databaseReference
                .updateChildren(mapDialog)
                .addOnSuccessListener {
                    setInMainList(user, message, messageKey.toString())
                }
        }

    private fun setInMainList(user: UserModel, message: String, messageKey: String) {
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_MESSAGE_KEY] = messageKey
        mapMessage[CHILD_FROM] = auth.currentUser?.uid.toString()
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_TIMESTAMP] = System.currentTimeMillis()
        mapMessage[CHILD_FULL_NAME] = user.full_name
        mapMessage[CHILD_USERNAME] = user.username
        mapMessage[CHILD_URL] = user.url
        mapMessage[CHILD_ID] = user.id
        databaseReference.child(NODE_CHAT_LIST).child(auth.currentUser?.uid.toString())
            .child(user.id).updateChildren(mapMessage)
        databaseReference.child(NODE_CHAT_LIST).child(user.id)
            .child(auth.currentUser?.uid.toString()).updateChildren(mapMessage)

    }

}