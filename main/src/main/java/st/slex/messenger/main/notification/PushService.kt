package st.slex.messenger.main.notification

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.i(TAG, remoteMessage.data.toString())
    }

    private fun sendRegistrationToServer(token: String) = Unit

    companion object {
        const val INTENT_FILTER = "MESSAGING_EVENT"
        const val KEY_ACTION = "ACTION"
        const val ACTION_SHOW_MESSAGE = "SHOW_MESSAGE"
    }
}