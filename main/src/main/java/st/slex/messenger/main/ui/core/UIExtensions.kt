package st.slex.messenger.main.ui.core

import android.view.View
import androidx.core.view.isGone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UIExtensions {

    suspend fun View.changeVisibility(): Unit = withContext(Dispatchers.Main) {
        visibility = if (isGone) View.GONE else View.VISIBLE
    }
}
