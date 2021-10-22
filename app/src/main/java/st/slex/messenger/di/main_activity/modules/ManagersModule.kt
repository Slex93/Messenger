package st.slex.messenger.di.main_activity.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.core.CompleteTaskListener
import st.slex.messenger.data.core.ValueSnapshotListener
import st.slex.messenger.data.main_activity.ActivityContactsUpdater
import st.slex.messenger.ui.main.ContactsManager

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