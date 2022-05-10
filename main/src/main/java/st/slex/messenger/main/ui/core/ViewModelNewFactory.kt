package st.slex.messenger.main.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelNewFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelMap[modelClass]?.let {
        it.get() as T
    } ?: viewModelMap.filter {
        modelClass.isAssignableFrom(it.key)
    }.firstNotNullOfOrNull { it.value }?.let {
        it.get() as T
    } ?: throw IllegalArgumentException("Unknown model class $modelClass")
}