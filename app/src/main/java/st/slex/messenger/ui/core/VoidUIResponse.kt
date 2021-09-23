package st.slex.messenger.ui.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.VoidResult
import javax.inject.Inject


@ExperimentalCoroutinesApi
interface VoidUIResponse {

    suspend fun create(flow: Flow<VoidResult>): Flow<VoidUIResult>

    class Base @Inject constructor() : VoidUIResponse {

        override suspend fun create(flow: Flow<VoidResult>): Flow<VoidUIResult> = callbackFlow {
            flow.map {
                trySendBlocking(it)
            }
            awaitClose { }
        }

        private suspend inline fun Flow<VoidResult>.map(
            crossinline function: (VoidUIResult) -> Unit
        ) = try {
            this.collect {
                when (it) {
                    is VoidResult.Success -> function(VoidUIResult.Success)
                    is VoidResult.Failure -> function(VoidUIResult.Failure(it.exception))
                }
            }
        } catch (exception: Exception) {
            function(VoidUIResult.Failure(exception))
        }
    }
}