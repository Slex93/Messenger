package st.slex.messenger.core

sealed interface Resource<T> {

    fun <U> map(mapper: Mapper.ToUI<T, U>): U

    class Success<T>(val data: T? = null) : Resource<T> {
        override fun <U> map(mapper: Mapper.ToUI<T, U>): U = mapper.map(data)
    }

    class Failure<T>(val exception: Exception) : Resource<T> {
        override fun <U> map(mapper: Mapper.ToUI<T, U>): U =
            mapper.map(exception = exception)
    }

    class Loading<T>(val data: T? = null) : Resource<T> {
        override fun <U> map(mapper: Mapper.ToUI<T, U>): U = mapper.map()
    }
}