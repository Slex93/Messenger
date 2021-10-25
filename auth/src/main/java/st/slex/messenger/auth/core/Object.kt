package st.slex.messenger.auth.core

interface Object<out T> {
    fun <U> map(mapper: Mapper.ToUI<in T, U>): U
}