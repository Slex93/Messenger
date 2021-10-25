package st.slex.messenger.auth.di

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.di.modules.*

@ExperimentalCoroutinesApi
@Module(
    includes = [
        ReferencesModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        EngineModule::class,
        InteractorModule::class,
        MapperModule::class,
        RepositoryModule::class
    ]
)
interface AuthModule