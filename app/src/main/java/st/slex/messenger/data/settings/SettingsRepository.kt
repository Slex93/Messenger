package st.slex.messenger.data.settings

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.core.VoidDataResult
import st.slex.messenger.utilites.CHILD_STATE
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject

interface SettingsRepository {
    suspend fun signOut(state: String): Flow<VoidDataResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference
    ) : SettingsRepository {

        override suspend fun signOut(state: String): Flow<VoidDataResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(Firebase.auth.uid.toString())
                .child(CHILD_STATE)
                .setValue(state)
            val listener = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    FirebaseAuth.AuthStateListener { auth ->
                        if (auth.currentUser?.uid == null) {
                            trySendBlocking(VoidDataResult.Success)
                        }
                    }
                    Firebase.auth.signOut()
                } else trySendBlocking(VoidDataResult.Failure(it.exception!!))
            }
            reference.addOnCompleteListener(listener)
            awaitClose { }
        }
    }
}