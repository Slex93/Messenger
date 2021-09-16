package st.slex.messenger.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class ActivityModule(val activity: Activity) {
    @Provides
    @Singleton
    fun providesActivity(): Activity = activity
}