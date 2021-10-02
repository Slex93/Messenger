package st.slex.messenger.core

abstract class Abstract {

    interface Object<T, M : Mapper> {

        fun map(mapper: M): T
    }

    interface UiObject {
        class Empty : UiObject
    }

    interface Mapper {

        interface Data<S, R> : Mapper {
            fun map(data: S): R
        }

        interface DataToUi<D, U> : Data<D, U> {
            fun map(exception: Exception): U
        }

        class Empty : Mapper
    }
}