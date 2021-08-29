package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.utilites.CHILD_STATE
import st.slex.messenger.utilites.NODE_MESSAGES
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.EventResponse
import javax.inject.Inject

class SingleChatRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : SingleChatRepository {

    override suspend fun getStatus(uid: String): Flow<EventResponse> = service.valueEventFlow(
        databaseReference.child(NODE_USER).child(uid).child(CHILD_STATE)
    )

    suspend fun getMessage(uid: String, limitToLast: Int): Flow<ChildEventResponse> =
        service.childEventFlow(
            databaseReference.child(NODE_MESSAGES).child(auth.uid.toString()), limitToLast
        )

}