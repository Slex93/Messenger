package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import st.slex.messenger.domain.interactor.impl.LoginInteractorImpl
import st.slex.messenger.domain.interactor.interf.LoginInteractor

@Module
interface InteractorModule {
    @Binds
    fun bindsLoginInteractor(interactor: LoginInteractorImpl): LoginInteractor
}