package st.slex.messenger.di.auth

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.modules.ReferencesModule
import st.slex.messenger.di.application.modules.ViewModelFactoryModule
import st.slex.messenger.di.auth.modules.*

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