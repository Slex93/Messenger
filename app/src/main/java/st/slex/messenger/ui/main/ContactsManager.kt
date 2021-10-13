package st.slex.messenger.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.model.firebase.FirebaseContactModel
import st.slex.messenger.utilites.PERMISSION_REQUEST
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsManager {

    suspend fun setContacts(): Flow<List<FirebaseContactModel>>

    class Base @Inject constructor(
        private val activity: MainActivity
    ) : ContactsManager {

        @SuppressLint("Range")
        override suspend fun setContacts(): Flow<List<FirebaseContactModel>> =
            callbackFlow {
                checkPermission(Manifest.permission.READ_CONTACTS).collect { permission ->
                    responseContacts(permission) {
                        trySendBlocking(it)
                    }
                }
                awaitClose {}
            }

        private suspend fun checkPermission(permission: String): Flow<Boolean> = callbackFlow {
            responsePermission(permission) { trySendBlocking(it) }
            awaitClose { }
        }

        @SuppressLint("Range")
        private inline fun responseContacts(
            permission: Boolean,
            crossinline function: (List<FirebaseContactModel>) -> Unit
        ): Unit = if (permission) {
            val list = mutableListOf<FirebaseContactModel>()
            val cursor = activity.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            cursor?.let { item ->
                while (item.moveToNext()) {
                    val username =
                        item.getString(item.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phone =
                        item.getString(item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val setPhone = phone.replace(Regex("[\\s,-]"), "")
                    val contactModel =
                        FirebaseContactModel(phone = setPhone, username = username)
                    list.add(contactModel)
                }
            }
            cursor?.close()
            function(list)
        } else function(emptyList())

        private inline fun responsePermission(
            permission: String,
            crossinline function: (Boolean) -> Unit
        ) = if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            function(true)

        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST)
            function(false)
        }
    }
}