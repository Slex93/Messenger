package st.slex.messenger.ui.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.core.DataResult
import javax.inject.Inject


@ExperimentalCoroutinesApi
interface VoidUIResponse {

    suspend fun create(flow: Flow<DataResult<*>>): Flow<UIResult<*>>

    class Base @Inject constructor() : VoidUIResponse {

        override suspend fun create(flow: Flow<DataResult<*>>): Flow<UIResult<*>> = callbackFlow {
            flow.map {
                trySendBlocking(it)
            }
            awaitClose { }
        }

        private suspend inline fun Flow<DataResult<*>>.map(
            crossinline function: (UIResult<*>) -> Unit
        ) = try {
            this.collect {
                when (it) {
                    is DataResult.Success -> function(UIResult.Success(null))
                    is DataResult.Failure -> function(UIResult.Failure(it.exception))
                }
            }
        } catch (exception: Exception) {
            function(UIResult.Failure(exception))
        }
    }
}