package st.slex.messenger.auth.di

import dagger.Module
import st.slex.messenger.auth.di.modules.*

@Module(
    includes = [
        ReferencesModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        UtilsModule::class,
        InteractorModule::class,
        MapperModule::class,
        RepositoryModule::class
    ]
)
interface AuthModule