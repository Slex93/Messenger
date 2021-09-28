package st.slex.messenger.di.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.core.ViewModelFactory
import st.slex.messenger.data.auth.AuthRepository
import st.slex.messenger.di.key.ViewModelKey
import st.slex.messenger.domain.auth.AuthInteractor
import st.slex.messenger.ui.auth.AuthViewModel
import st.slex.messenger.ui.core.VoidUIResponse

@ExperimentalCoroutinesApi
@Module
interface AuthInterfaceModule {

    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel::class)
    fun bindsLoginViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractor.Base): AuthInteractor

    @Binds
    fun bindsLoginRepository(repository: AuthRepository.Base): AuthRepository

    @Binds
    fun bindsVoidUIResponse(response: VoidUIResponse.Base): VoidUIResponse

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}