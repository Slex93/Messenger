package st.slex.messenger.core

import st.slex.messenger.auth.core.Mapper

interface Mapper {

    interface Data<S, R> : Mapper {
        fun map(data: S): R
    }

    interface ToUI<D, U> : Mapper {
        fun map(data: D): U
        fun map(exception: Exception): U
        fun map(): U
    }
}