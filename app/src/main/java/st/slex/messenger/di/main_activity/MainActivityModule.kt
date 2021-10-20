package st.slex.messenger.di.main_activity

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.modules.ReferencesModule
import st.slex.messenger.di.application.modules.ViewModelFactoryModule
import st.slex.messenger.di.main_activity.modules.ManagersModule
import st.slex.messenger.di.main_activity.modules.MapperModule
import st.slex.messenger.di.main_activity.modules.RepositoryModule
import st.slex.messenger.di.main_activity.modules.ViewModelModule

@ExperimentalCoroutinesApi
@Module(
    includes = [
        ManagersModule::class,
        MapperModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class
    ]
)
class MainActivityModule