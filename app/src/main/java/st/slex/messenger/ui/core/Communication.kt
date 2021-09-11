package st.slex.messenger.ui.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import st.slex.messenger.core.Abstract

interface Communication<T> : Abstract.Mapper.Data<T, T> {

    abstract class Base<T : Any> : Communication<T> {
        override fun map(data: T): Flow<T> = flowOf(data).flowOn(Dispatchers.IO)
    }
}