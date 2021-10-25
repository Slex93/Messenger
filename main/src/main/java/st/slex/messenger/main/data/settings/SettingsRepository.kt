package st.slex.messenger.main.data.settings

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.core.FirebaseConstants.CHILD_STATE
import st.slex.messenger.core.FirebaseConstants.NODE_USER
import st.slex.messenger.core.Resource
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface SettingsRepository {

    suspend fun signOut(state: String): Resource<Nothing?>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference
    ) : SettingsRepository {

        override suspend fun signOut(state: String): Resource<Nothing?> =
            suspendCoroutine { continuation ->
                databaseReference
                    .child(NODE_USER)
                    .child(Firebase.auth.uid.toString())
                    .child(CHILD_STATE)
                    .setValue(state)
                    .addOnSuccessListener {
                        Firebase.auth.signOut()
                        continuation.resumeWith(Result.success(Resource.Success(null)))
                    }.addOnFailureListener {
                        continuation.resumeWith(Result.success(Resource.Failure(it)))
                    }
            }
    }
}