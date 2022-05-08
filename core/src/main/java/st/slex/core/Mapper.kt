package st.slex.core

interface Mapper {

    interface Data<in S, out R> : Mapper {
        fun map(data: S): R
    }

    interface ToUI<in D, out U> : Data<D, U> {
        fun map(exception: Exception): U
        fun map(): U
    }
}