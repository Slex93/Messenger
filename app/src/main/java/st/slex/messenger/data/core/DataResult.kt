package st.slex.messenger.data.core

import st.slex.messenger.core.Abstract

sealed interface DataResult<D> {

    fun <U> map(mapper: Abstract.Mapper.DataToUi<D, U>): U

    class Success<T>(val data: T) : DataResult<T> {
        override fun <U> map(mapper: Abstract.Mapper.DataToUi<T, U>) = mapper.map(data)
    }

    class Failure<T>(val exception: Exception) : DataResult<T> {
        override fun <U> map(mapper: Abstract.Mapper.DataToUi<T, U>): U =
            mapper.map(exception)
    }
}