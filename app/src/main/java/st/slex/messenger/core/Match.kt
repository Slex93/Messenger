package st.slex.messenger.core

interface Match<T> {
    fun matches(data: T): Boolean
}