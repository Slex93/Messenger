package st.slex.messenger.data.chat

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.core.AppChildEventListener
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.core.Response
import st.slex.messenger.data.profile.UserModel
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.funs.getThisValue
import javax.inject.Inject

interface SingleChatRepository {
    suspend fun getUser(uid: String): Flow<Response<UserModel>>
    suspend fun getCurrentUser(uid: String): Flow<Response<UserModel>>
    suspend fun getMessages(uid: String, limitToLast: Int): Flow<Response<MessageModel>>
    suspend fun sendMessage(message: String, user: UserModel, currentUser: UserModel)

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val auth: FirebaseUser
    ) : SingleChatRepository {

        override suspend fun getUser(uid: String): Flow<Response<UserModel>> = callbackFlow {
            val reference = databaseReference.child(NODE_USER).child(uid)
            val listener = AppValueEventListener({ snapshot ->
                val user = snapshot.getThisValue<UserModel>()
                databaseReference
                    .child(NODE_CONTACT)
                    .child(auth.uid)
                    .child(uid)
                    .child(CHILD_FULL_NAME)
                    .addListenerForSingleValueEvent(
                        AppValueEventListener({
                            trySendBlocking(Response.Success(user.copy(full_name = it.value.toString())))
                        }, {
                            trySendBlocking(Response.Failure(it))
                        })
                    )
            }, {
                trySendBlocking(Response.Failure(it))
            })
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun getMessages(
            uid: String,
            limitToLast: Int
        ): Flow<Response<MessageModel>> =
            callbackFlow {
                val reference = databaseReference
                    .child(NODE_CHAT)
                    .child(auth.uid)
                    .child(uid)
                    .limitToLast(limitToLast)
                val listener = AppChildEventListener({ snapshot ->
                    trySendBlocking(Response.Success(snapshot.getThisValue()))
                }, { exception ->
                    trySendBlocking(Response.Failure(exception))
                })
                reference.addChildEventListener(listener)
                awaitClose { reference.removeEventListener(listener) }
            }

        override suspend fun getCurrentUser(uid: String): Flow<Response<UserModel>> = callbackFlow {
            val reference = databaseReference.child(NODE_USER).child(auth.uid)
            val listener = AppValueEventListener({ snapshot ->
                val currentUser = snapshot.getThisValue<UserModel>()
                if (currentUser.full_name.isEmpty()) {
                    databaseReference
                        .child(NODE_CONTACT)
                        .child(uid)
                        .child(auth.uid)
                        .child(CHILD_FULL_NAME)
                        .addValueEventListener(
                            AppValueEventListener({
                                val fullName = it.getThisValue<String>()
                                val name = if (fullName.isEmpty()) {
                                    currentUser.username
                                } else {
                                    fullName
                                }
                                trySendBlocking(Response.Success(currentUser.copy(full_name = name)))
                            }, {
                                trySendBlocking(Response.Failure(it))
                            })
                        )
                } else {
                    trySendBlocking(Response.Success(currentUser))
                }
            }, {
                trySendBlocking(Response.Failure(it))
            })
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun sendMessage(
            message: String,
            user: UserModel,
            currentUser: UserModel
        ): Unit =
            withContext(Dispatchers.IO) {
                val refDialogUser = "$NODE_CHAT/${auth.uid}/${user.id}"
                val refDialogReceivingUser = "$NODE_CHAT/${user.id}/${auth.uid}"
                val messageKey = databaseReference.child(refDialogUser).push().key
                val mapMessage = hashMapOf<String, Any>()
                mapMessage[CHILD_FROM] = auth.uid
                mapMessage[CHILD_TEXT] = message
                mapMessage[CHILD_TIMESTAMP] = System.currentTimeMillis()
                val mapDialog = hashMapOf<String, Any>()
                mapDialog["$refDialogUser/$messageKey"] = mapMessage
                mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage
                databaseReference
                    .updateChildren(mapDialog)
                    .addOnSuccessListener {
                        setInChatList(user, currentUser, message, messageKey.toString())
                    }
            }

        private fun setInChatList(
            user: UserModel,
            currentUser: UserModel,
            message: String,
            messageKey: String,
        ) {
            val mapUser = mutableMapOf<String, Any>(
                CHILD_MESSAGE_KEY to messageKey,
                CHILD_FROM to currentUser.id,
                CHILD_TEXT to message,
                CHILD_TIMESTAMP to System.currentTimeMillis(),
                CHILD_FULL_NAME to user.full_name,
                CHILD_USERNAME to user.username,
                CHILD_URL to user.url,
                CHILD_ID to user.id
            )
            val mapReceiver = mutableMapOf<String, Any>(
                CHILD_MESSAGE_KEY to messageKey,
                CHILD_FROM to currentUser.id,
                CHILD_TEXT to message,
                CHILD_TIMESTAMP to System.currentTimeMillis(),
                CHILD_FULL_NAME to currentUser.full_name,
                CHILD_USERNAME to currentUser.username,
                CHILD_URL to currentUser.url
            )
            databaseReference.child(NODE_CHAT_LIST).child(auth.uid)
                .child(user.id).updateChildren(mapUser)
            databaseReference.child(NODE_CHAT_LIST).child(user.id)
                .child(auth.uid).updateChildren(mapReceiver)

        }

    }
}