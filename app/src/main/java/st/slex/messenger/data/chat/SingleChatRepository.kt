package st.slex.messenger.data.chat

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.utilites.*
import javax.inject.Inject

interface SingleChatRepository {

    suspend fun sendMessage(
        message: String,
        user: UserUI,
        currentUser: UserUI
    ): Flow<Resource<Nothing?>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val auth: FirebaseUser
    ) : SingleChatRepository {

        override suspend fun sendMessage(
            message: String,
            user: UserUI,
            currentUser: UserUI
        ): Flow<Resource<Nothing?>> = callbackFlow {
            val refDialogUser = "$NODE_CHAT/${auth.uid}/${user.id()}"
            val refDialogReceivingUser = "$NODE_CHAT/${user.id()}/${auth.uid}"
            val messageKey = databaseReference.child(refDialogUser).push().key
            val mapMessage = hashMapOf<String, Any>()
            mapMessage[CHILD_FROM] = auth.uid
            mapMessage[CHILD_TEXT] = message
            mapMessage[CHILD_TIMESTAMP] = System.currentTimeMillis()
            val mapDialog = hashMapOf<String, Any>()
            mapDialog["$refDialogUser/$messageKey"] = mapMessage
            mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

            val task = databaseReference
                .updateChildren(mapDialog)

            val taskUser = databaseReference.child(NODE_CHAT_LIST).child(auth.uid)
                .child(user.id()).updateChildren(
                    getMapUser(
                        user,
                        currentUser,
                        message,
                        messageKey.toString()
                    )
                )

            val taskReceiver = databaseReference.child(NODE_CHAT_LIST).child(user.id())
                .child(auth.uid).updateChildren(
                    getMapReceiver(
                        currentUser,
                        message,
                        messageKey.toString()
                    )
                )

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    taskUser.addOnCompleteListener { uTask ->
                        if (it.isSuccessful) {
                            taskReceiver.addOnCompleteListener { rTask ->
                                if (rTask.isSuccessful) {
                                    trySendBlocking(Resource.Success(null))
                                } else {
                                    trySendBlocking(Resource.Failure(rTask.exception!!))
                                }
                            }
                        } else {
                            trySendBlocking(Resource.Failure(uTask.exception!!))
                        }
                    }
                } else {
                    trySendBlocking(Resource.Failure(it.exception!!))
                }
            }

            awaitClose { }
        }

        private fun getMapUser(
            user: UserUI,
            currentUser: UserUI,
            message: String,
            messageKey: String
        ): Map<String, Any> = mapOf(
            CHILD_MESSAGE_KEY to messageKey,
            CHILD_FROM to currentUser.id(),
            CHILD_TEXT to message,
            CHILD_TIMESTAMP to System.currentTimeMillis(),
            CHILD_FULL_NAME to user.fullName(),
            CHILD_USERNAME to user.username(),
            CHILD_URL to user.url(),
            CHILD_ID to user.id()
        )

        private fun getMapReceiver(
            currentUser: UserUI,
            message: String,
            messageKey: String
        ): Map<String, Any> = mapOf(
            CHILD_MESSAGE_KEY to messageKey,
            CHILD_FROM to currentUser.id(),
            CHILD_TEXT to message,
            CHILD_TIMESTAMP to System.currentTimeMillis(),
            CHILD_FULL_NAME to currentUser.fullName(),
            CHILD_USERNAME to currentUser.username(),
            CHILD_URL to currentUser.url()
        )
    }
}