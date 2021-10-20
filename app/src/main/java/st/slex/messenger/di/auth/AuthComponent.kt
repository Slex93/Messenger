package st.slex.messenger.di.auth

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.AppComponent
import st.slex.messenger.di.scope.AuthScope
import st.slex.messenger.ui.auth.AuthActivity
import st.slex.messenger.ui.core.BaseAuthFragment

@AuthScope
@ExperimentalCoroutinesApi
@Component(
    dependencies = [AppComponent::class],
    modules = [AuthModule::class]
)
interface AuthComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: AuthActivity): Builder
        fun appComponent(appComponent: AppComponent): Builder
        fun create(): AuthComponent
    }

    fun inject(fragment: BaseAuthFragment)
}