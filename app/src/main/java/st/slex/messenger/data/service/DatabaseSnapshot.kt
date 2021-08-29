package st.slex.messenger.data.service

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.EventResponse

interface DatabaseSnapshot {
    suspend fun valueEventFlow(databaseReference: DatabaseReference): Flow<EventResponse>
    suspend fun childEventFlow(databaseReference: DatabaseReference): Flow<ChildEventResponse>
}