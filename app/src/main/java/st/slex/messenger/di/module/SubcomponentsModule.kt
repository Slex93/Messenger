package st.slex.messenger.di.module

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.component.AuthComponent

@ExperimentalCoroutinesApi
@Module(subcomponents = [AuthComponent::class])
interface SubcomponentsModule