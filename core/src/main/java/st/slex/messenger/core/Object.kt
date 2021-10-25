package st.slex.messenger.core

interface Object<out T> {
    fun <U> map(mapper: Mapper.ToUI<in T, U>): U
}