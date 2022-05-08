package st.slex.messenger.main.data.single_chat

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.core.FirebaseConstants.CHILD_FROM
import st.slex.core.FirebaseConstants.CHILD_ID
import st.slex.core.FirebaseConstants.CHILD_MESSAGE
import st.slex.core.FirebaseConstants.CHILD_TIMESTAMP
import st.slex.core.FirebaseConstants.NODE_CHATS
import st.slex.core.FirebaseConstants.NODE_MESSAGES
import st.slex.core.FirebaseConstants.NODE_TOKENS
import st.slex.core.Resource
import st.slex.messenger.main.data.core.ValueSnapshotListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface SingleChatRepository {

    suspend fun sendMessage(receiverId: String, message: String): Resource<Nothing?>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser,
        private val listener: ValueSnapshotListener
    ) : SingleChatRepository {

        override suspend fun sendMessage(
            receiverId: String, message: String
        ): Resource<Nothing?> = suspendCoroutine { continuation ->

            val referenceSenderMessages = messagesReference.child(auth.uid).child(receiverId)
            val referenceReceiverMessages = messagesReference.child(receiverId).child(auth.uid)
            val referenceChatsSender = chatsReference.child(auth.uid).child(receiverId)
            val referenceChatsReceiver = chatsReference.child(receiverId).child(auth.uid)

            val messageKey = referenceSenderMessages.push().key.toString()

            val mapSender = mapMessage(message, receiverId)
            val mapReceiver = mapMessage(message, auth.uid)
            val taskMessageSender =
                referenceSenderMessages.child(messageKey).updateChildren(mapSender)
            val taskMessageReceiver =
                referenceReceiverMessages.child(messageKey).updateChildren(mapReceiver)

            val taskChatsSender = referenceChatsSender.updateChildren(mapSender)
            val taskChatsReceiver = referenceChatsReceiver.updateChildren(mapReceiver)

            notificationSender(receiverId, messageKey, mapReceiver)

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

        private fun notificationSender(
            receiverId: String,
            messageKey: String,
            mapReceiver: Map<String, String>
        ) {
            val notificationReference: DatabaseReference =
                reference.child(NODE_TOKENS).child(receiverId)
            val listener = tokenListener(messageKey, mapReceiver)
            notificationReference.addListenerForSingleValueEvent(listener)
        }

        private fun tokenListener(
            messageKey: String, mapReceiver: Map<String, String>
        ): ValueEventListener = listener.singleEventListener(String::class) {
            when (it) {
                is Resource.Success -> sendNotification(it.data, messageKey, mapReceiver)
                else -> Unit
            }
        }

        private fun sendNotification(
            receiverToken: String,
            messageKey: String,
            mapReceiver: Map<String, String>
        ) {
            val remoteMessage = remoteMessage(receiverToken) {
                setMessageId(messageKey)
                setData(mapReceiver).build()
            }
            Firebase.messaging.send(remoteMessage)
        }

        private val messagesReference: DatabaseReference by lazy {
            reference.child(NODE_MESSAGES)
        }

        private val chatsReference: DatabaseReference by lazy {
            reference.child(NODE_CHATS)
        }

        private fun mapMessage(message: String, id: String): Map<String, String> = mapOf(
            CHILD_ID to id,
            CHILD_FROM to auth.uid,
            CHILD_MESSAGE to message,
            CHILD_TIMESTAMP to System.currentTimeMillis().toString()
        )
    }
}
