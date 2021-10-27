package st.slex.messenger.main.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.data.core.CompleteTaskListener
import st.slex.messenger.main.data.core.ValueSnapshotListener
import st.slex.messenger.main.data.main_activity.ActivityContactsUpdater
import st.slex.messenger.main.ui.ContactsManager

@Module
interface ManagersModule {

    @ExperimentalCoroutinesApi
    @Binds
    fun bindsContactsManager(manager: ContactsManager.Base): ContactsManager

    @Binds
    fun bindsValueSnapshotListener(listener: ValueSnapshotListener.Base): ValueSnapshotListener

    @Binds
    fun bindsTaskListener(listener: CompleteTaskListener.Base): CompleteTaskListener

    @Binds
    fun bindsActivityContactsUpdater(updater: ActivityContactsUpdater.Base): ActivityContactsUpdater
}