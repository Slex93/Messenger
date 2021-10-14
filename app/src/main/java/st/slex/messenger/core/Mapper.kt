package st.slex.messenger.core

interface Mapper {

    interface Data<S, R> : Mapper {
        fun map(data: S): R
    }

    interface DataToUi<D, U> : Data<D, U> {
        fun map(exception: Exception): U
    }

    class Empty : Mapper
}