package st.slex.messenger.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import st.slex.messenger.core.ViewModelFactory

@Module
interface ViewModelFactoryModule {
    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}