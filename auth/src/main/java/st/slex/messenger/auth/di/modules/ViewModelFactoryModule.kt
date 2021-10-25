package st.slex.messenger.auth.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.ui.ViewModelFactory

@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}