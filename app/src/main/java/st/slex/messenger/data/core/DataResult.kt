package st.slex.messenger.data.core

import st.slex.messenger.core.Mapper

sealed interface DataResult<D> {

    fun <U> map(mapper: Mapper.DataToUi<D, U>): U

    class Success<T>(val data: T) : DataResult<T> {
        override fun <U> map(mapper: Mapper.DataToUi<T, U>) = mapper.map(data)
    }

    class Failure<T>(val exception: Exception) : DataResult<T> {
        override fun <U> map(mapper: Mapper.DataToUi<T, U>): U =
            mapper.map(exception)
    }
}