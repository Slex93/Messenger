package st.slex.core

interface Object<out T> {
    fun <U> map(mapper: Mapper.ToUI<in T, U>): U
}