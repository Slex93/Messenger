package st.slex.messenger.data.service

import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.EventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi

class DatabaseSnapshotImpl @Inject constructor() : DatabaseSnapshot {
    override suspend fun valueEventFlow(databaseReference: DatabaseReference): Flow<EventResponse> =
        callbackFlow {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySendBlocking(EventResponse.Success(snapshot)).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(EventResponse.Cancelled(error)).isFailure
                }
            }
            databaseReference.addValueEventListener(valueEventListener)
            awaitClose {
                databaseReference.removeEventListener(valueEventListener)
            }
        }

    override suspend fun childEventFlow(databaseReference: DatabaseReference): Flow<ChildEventResponse> =
        callbackFlow {
            val eventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    trySendBlocking(ChildEventResponse.Added(snapshot, previousChildName)).isSuccess
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    trySendBlocking(ChildEventResponse.Added(snapshot, previousChildName)).isSuccess
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    trySendBlocking(ChildEventResponse.Removed(snapshot)).isSuccess
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    trySendBlocking(ChildEventResponse.Moved(snapshot, previousChildName)).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(ChildEventResponse.Cancelled(error)).isFailure
                }
            }
            databaseReference.addChildEventListener(eventListener)
            awaitClose {
                databaseReference.removeEventListener(eventListener)
            }
        }
}
