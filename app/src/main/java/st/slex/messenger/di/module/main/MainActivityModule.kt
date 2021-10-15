package st.slex.messenger.di.module.main

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(
    includes = [
        ManagersModule::class,
        MapperModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
class MainActivityModule