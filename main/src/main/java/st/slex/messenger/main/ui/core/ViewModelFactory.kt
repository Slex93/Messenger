package st.slex.messenger.main.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelMap[modelClass]
        ?.let(::viewModelMapper)
        ?: modelClass.filterViewModelMap?.let(::viewModelMapper)
        ?: throw IllegalArgumentException("Unknown model class $modelClass")

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> viewModelMapper(provider: Provider<ViewModel>): T =
        provider.get() as T

    private val <T> Class<T>.filterViewModelMap: Provider<ViewModel>?
        get() = viewModelMap.filter {
            isAssignableFrom(it.key)
        }.firstNotNullOfOrNull { it.value }
}