package st.slex.messenger.core

import st.slex.messenger.auth.core.Mapper
import st.slex.messenger.auth.core.TextMapper

interface TextMapper<T> : Mapper.Data<String, T> {

    interface Void : TextMapper<Unit> {
        override fun map(data: String) = Unit
    }
}