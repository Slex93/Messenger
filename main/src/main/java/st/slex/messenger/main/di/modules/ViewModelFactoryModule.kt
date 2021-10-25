package st.slex.messenger.main.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import st.slex.messenger.main.ui.core.ViewModelFactory

@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}