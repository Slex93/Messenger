package st.slex.messenger.data.service.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.service.interf.StateService
import st.slex.messenger.utilites.CHILD_STATE
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class StateServiceImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : StateService {

    override suspend fun changeState(state: String) = callbackFlow {
        val event = databaseReference.child(NODE_USER).child(auth.uid.toString())
            .child(CHILD_STATE).setValue(state).addOnSuccessListener {
                trySendBlocking(VoidResponse.Success)
            }.addOnFailureListener {
                trySendBlocking(VoidResponse.Failure(it))
            }
        awaitClose { event }
    }
}