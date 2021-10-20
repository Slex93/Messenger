package st.slex.messenger.core

interface TextMapper<T> : Mapper.Data<String, T> {

    interface Void : TextMapper<Unit> {
        override fun map(data: String) = Unit
    }
}