package st.slex.messenger.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SaveText(private val dataSource: Save<String>) : TextMapper.Void {
    override fun map(data: String) = flow { emit(dataSource.save(data)) }.flowOn(Dispatchers.IO)
}