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
            val valueEventListener = valueEventListener({
                trySendBlocking(EventResponse.Success(it)).isSuccess
            }, {
                trySendBlocking(EventResponse.Cancelled(it)).isFailure
            })
            databaseReference.addValueEventListener(valueEventListener)
            awaitClose {
                databaseReference.removeEventListener(valueEventListener)
            }
        }

    override suspend fun singleValueEventFlow(databaseReference: DatabaseReference): Flow<EventResponse> =
        callbackFlow {
            val valueEventListener = valueEventListener({
                trySendBlocking(EventResponse.Success(it)).isSuccess
            }, {
                trySendBlocking(EventResponse.Cancelled(it)).isFailure
            })
            databaseReference.addListenerForSingleValueEvent(valueEventListener)
            awaitClose {
                databaseReference.removeEventListener(valueEventListener)
            }
        }

    override suspend fun childEventFlow(
        databaseReference: DatabaseReference,
        limitToLast: Int
    ): Flow<ChildEventResponse> =
        callbackFlow {
            val eventListener = childrenEventListener({ snapshot, previousChildName ->
                trySendBlocking(ChildEventResponse.Added(snapshot, previousChildName)).isSuccess
            }, { snapshot, previousChildName ->
                trySendBlocking(ChildEventResponse.Added(snapshot, previousChildName)).isSuccess
            }, { snapshot ->
                trySendBlocking(ChildEventResponse.Removed(snapshot)).isSuccess
            }, { snapshot, previousChildName ->
                trySendBlocking(ChildEventResponse.Moved(snapshot, previousChildName)).isSuccess
            }, { error ->
                trySendBlocking(ChildEventResponse.Cancelled(error)).isFailure
            })
            databaseReference.limitToLast(limitToLast).addChildEventListener(eventListener)
            awaitClose {
                databaseReference.removeEventListener(eventListener)
            }
        }

    private inline fun childrenEventListener(
        crossinline onChildAdded: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        crossinline onChildChanged: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        crossinline onChildRemoved: (snapshot: DataSnapshot) -> Unit,
        crossinline onChildMoved: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        crossinline onCancelled: (error: DatabaseError) -> Unit,
    ) = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) =
            onChildAdded(snapshot, previousChildName)

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) =
            onChildChanged(snapshot, previousChildName)

        override fun onChildRemoved(snapshot: DataSnapshot) = onChildRemoved(snapshot)
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) =
            onChildMoved(snapshot, previousChildName)

        override fun onCancelled(error: DatabaseError) = onCancelled(error)
    }

    private inline fun valueEventListener(
        crossinline onDataChange: (snapshot: DataSnapshot) -> Unit,
        crossinline onCancelled: (error: DatabaseError) -> Unit
    ) = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot): Unit = onDataChange(snapshot)
        override fun onCancelled(error: DatabaseError): Unit = onCancelled(error)
    }


}
