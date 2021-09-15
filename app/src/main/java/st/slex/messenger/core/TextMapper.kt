package st.slex.messenger.core

interface TextMapper<T> : Abstract.Mapper.Data<String, T> {
    interface Void : TextMapper<Unit> {
        override fun map(data: String) = Unit
    }
}