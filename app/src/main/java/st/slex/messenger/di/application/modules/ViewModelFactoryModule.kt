package st.slex.messenger.di.application.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import st.slex.messenger.ui.core.ViewModelFactory

@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}