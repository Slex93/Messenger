package st.slex.messenger.main.ui.core

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UIExtensions {

    suspend fun View.changeVisibility(): Unit = withContext(Dispatchers.Main) {
        visibility = if (visibility == View.VISIBLE) {
            View.INVISIBLE
        } else View.VISIBLE
    }
}
