package st.slex.messenger.data.repository.impl

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.repository.interf.SettingsRepository
import st.slex.messenger.utilites.CHILD_STATE
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SettingsRepository {

    override suspend fun signOut(state: String): Flow<VoidResponse> = callbackFlow {
        val reference = databaseReference
            .child(NODE_USER)
            .child(Firebase.auth.uid.toString())
            .child(CHILD_STATE)
            .setValue(state)
        val listener = OnCompleteListener<Void> {
            if (it.isSuccessful) {
                Firebase.auth.signOut()
                trySendBlocking(VoidResponse.Success)
            } else trySendBlocking(VoidResponse.Failure(it.exception!!))
        }
        awaitClose { reference.addOnCompleteListener(listener) }
    }
}