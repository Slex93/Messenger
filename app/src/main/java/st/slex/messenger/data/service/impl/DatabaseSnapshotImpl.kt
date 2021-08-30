package st.slex.messenger.data.service.impl

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.utilites.funs.childrenEventListener
import st.slex.messenger.utilites.funs.valueEventListener
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

}
