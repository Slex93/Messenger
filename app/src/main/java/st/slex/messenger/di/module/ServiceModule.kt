package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.service.impl.DatabaseSnapshotImpl
import st.slex.messenger.data.service.impl.StateServiceImpl
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.data.service.interf.StateService

@ExperimentalCoroutinesApi
@Module
interface ServiceModule {
    @Binds
    fun bindDatabaseSnapshot(databaseSnapshot: DatabaseSnapshotImpl): DatabaseSnapshot

    @Binds
    fun bindStateService(stateServiceImpl: StateServiceImpl): StateService
}