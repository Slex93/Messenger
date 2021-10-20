package st.slex.messenger.di.application

import dagger.Module
import st.slex.messenger.di.application.modules.ReferencesModule
import st.slex.messenger.di.application.modules.ViewModelFactoryModule

@Module(includes = [ReferencesModule::class, ViewModelFactoryModule::class])
class AppModule