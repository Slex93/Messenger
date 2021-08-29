package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.data.service.DatabaseSnapshotImpl

@ExperimentalCoroutinesApi
@Module
interface ServiceModule {
    @Binds
    fun bindDatabaseSnapshot(databaseSnapshot: DatabaseSnapshotImpl): DatabaseSnapshot
}