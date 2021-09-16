package st.slex.messenger.di.module

import android.app.Activity
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.activities.AuthActivity
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
interface ActivityModule {
    @Binds
    @Singleton
    fun providesActivity(activity: AuthActivity): Activity
}