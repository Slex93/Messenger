package st.slex.messenger.main.di

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.di.modules.*

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