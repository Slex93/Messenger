package st.slex.messenger.core

sealed class Resource<out T> : Object<T> {

    data class Success<T>(val data: T) : st.slex.messenger.core.Resource<T>() {
        override fun <U> map(mapper: Mapper.ToUI<in T, U>): U = mapper.map(data)
    }

    data class Failure<T>(val exception: Exception) : st.slex.messenger.core.Resource<T>() {
        override fun <U> map(mapper: Mapper.ToUI<in T, U>): U =
            mapper.map(exception = exception)
    }

    object Loading : st.slex.messenger.core.Resource<Nothing>() {
        override fun <U> map(mapper: Mapper.ToUI<in Nothing, U>): U = mapper.map()
    }
}