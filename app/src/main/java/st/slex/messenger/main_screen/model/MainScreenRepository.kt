package st.slex.messenger.main_screen.model

import androidx.lifecycle.LiveData
import st.slex.messenger.main_screen.model.base.MainMessage

class MainScreenRepository(db: MainScreenDatabase) {

    val mainMessage: LiveData<List<MainMessage>> = db.getMainList()

}