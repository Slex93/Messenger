package st.slex.messenger.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.AuthActivity

@Module
class ActivityModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideActivity(activity: AuthActivity): AuthActivity = activity
}