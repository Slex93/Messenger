package st.slex.messenger.di.module

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.component.AuthSubcomponent
import st.slex.messenger.di.component.MainActivitySubcomponent

@ExperimentalCoroutinesApi
@Module(subcomponents = [AuthSubcomponent::class, MainActivitySubcomponent::class])
class AppModule