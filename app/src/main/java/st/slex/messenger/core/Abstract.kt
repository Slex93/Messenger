package st.slex.messenger.core

import kotlinx.coroutines.flow.Flow

abstract class Abstract {

    interface Object<T, M : Mapper> {

        fun map(mapper: M): Flow<T>
    }

    interface UiObject {
        class Empty : UiObject
    }

    interface Mapper {
        interface Data<S, R> : Mapper {
            fun map(data: S): Flow<R>
        }

        class Empty : Mapper
    }
}