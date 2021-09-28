package st.slex.messenger.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.AuthActivity

@Module
interface ActivitiesModule {

    @ExperimentalCoroutinesApi
    @ContributesAndroidInjector(modules = [(ActivityModule::class)])
    fun bindMainActivity(): AuthActivity
}