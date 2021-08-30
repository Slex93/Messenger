package st.slex.messenger.data.service.interf

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.ValueEventResponse

interface DatabaseSnapshot {
    suspend fun valueEventFlow(databaseReference: DatabaseReference): Flow<ValueEventResponse>
    suspend fun singleValueEventFlow(databaseReference: DatabaseReference): Flow<ValueEventResponse>
    suspend fun childEventFlow(
        databaseReference: DatabaseReference,
        limitToLast: Int
    ): Flow<ChildEventResponse>
}