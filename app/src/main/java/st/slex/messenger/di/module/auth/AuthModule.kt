package st.slex.messenger.di.module.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import st.slex.messenger.data.auth.AuthRepository
import st.slex.messenger.di.key.ViewModelKey
import st.slex.messenger.domain.AuthInteractor
import st.slex.messenger.domain.LoginDomainMapper
import st.slex.messenger.ui.auth.AuthViewModel
import st.slex.messenger.ui.auth.LoginEngine
import st.slex.messenger.ui.auth.SendCodeEngine

@ExperimentalCoroutinesApi
@Module
interface AuthModule {

    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel::class)
    fun bindsLoginViewModel(viewModel: AuthViewModel): ViewModel

    @InternalCoroutinesApi
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractor.Base): AuthInteractor

    @InternalCoroutinesApi
    @Binds
    fun bindsLoginRepository(repository: AuthRepository.Base): AuthRepository

    @Binds
    fun bindsLoginEngine(engine: LoginEngine.Base): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngine.Base): SendCodeEngine

    @Binds
    fun bindsLoginMapper(mapper: LoginDomainMapper.Base): LoginDomainMapper
}