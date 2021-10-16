package st.slex.messenger.data.chat

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.core.Resource
import st.slex.messenger.utilites.*
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface SingleChatRepository {

    suspend fun sendMessage(receiverId: String, message: String): Resource<Nothing?>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser
    ) : SingleChatRepository {

        override suspend fun sendMessage(
            receiverId: String, message: String
        ): Resource<Nothing?> = suspendCoroutine { continuation ->

            val referenceSenderMessages = messagesReference.child(auth.uid).child(receiverId)
            val referenceReceiverMessages = messagesReference.child(receiverId).child(auth.uid)
            val referenceChatsSender = chatsReference.child(auth.uid).child(receiverId)
            val referenceChatsReceiver = chatsReference.child(receiverId).child(auth.uid)

            val messageKey = referenceSenderMessages.push().key.toString()

            val taskMessageSender = referenceSenderMessages
                .child(messageKey)
                .updateChildren(mapMessage(message))
            val taskMessageReceiver = referenceReceiverMessages
                .child(messageKey)
                .updateChildren(mapMessage(message))
            val taskChatsSender = referenceChatsSender.updateChildren(mapMessage(message))
            val taskChatsReceiver = referenceChatsReceiver.updateChildren(mapMessage(message))

            val failureListener = OnFailureListener {
                continuation.resumeWith(Result.success(Resource.Failure<Nothing>(it)))
            }
            val successListenerChatReceiver = OnSuccessListener<Void> {
                continuation.resumeWith(Result.success(Resource.Success(null)))
            }

            val successListenerChatSender = OnSuccessListener<Void> {
                taskChatsReceiver
                    .addOnSuccessListener(successListenerChatReceiver)
                    .addOnFailureListener(failureListener)
            }

            val successListenerMessageReceiver = OnSuccessListener<Void> {
                taskChatsSender
                    .addOnSuccessListener(successListenerChatSender)
                    .addOnFailureListener(failureListener)
            }

            val successListenerMessageSender = OnSuccessListener<Void> {
                taskMessageReceiver
                    .addOnSuccessListener(successListenerMessageReceiver)
                    .addOnFailureListener(failureListener)
            }

            taskMessageSender
                .addOnSuccessListener(successListenerMessageSender)
                .addOnFailureListener(failureListener)
        }

        private val messagesReference: DatabaseReference by lazy {
            reference.child(NODE_MESSAGES)
        }

        private val chatsReference: DatabaseReference by lazy {
            reference.child(NODE_CHATS)
        }

        private fun mapMessage(message: String): Map<String, Any> = mapOf(
            CHILD_FROM to auth.uid,
            CHILD_MESSAGE to message,
            CHILD_TIMESTAMP to System.currentTimeMillis().toString()
        )
    }
}
