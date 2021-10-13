package st.slex.messenger.di.module.main

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.core.VoidUIResponse

@ExperimentalCoroutinesApi
@Module
interface ResponseModule {

    @Binds
    fun bindsVoidUIResponse(response: VoidUIResponse.Base): VoidUIResponse
}