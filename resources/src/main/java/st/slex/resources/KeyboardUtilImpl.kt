package st.slex.resources

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.ref.WeakReference

class KeyboardUtilImpl constructor(
    private val activity: WeakReference<Activity>
) : KeyboardUtil {

    private val hidingView: View? by lazy {
        activity.get()?.currentFocus
    }

    private val inputMethodManager: InputMethodManager
        get() = activity.get()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    override fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(hidingView?.windowToken, HIDE_KEYBOARD)
    }

    companion object {
        private const val HIDE_KEYBOARD = InputMethodManager.RESULT_UNCHANGED_SHOWN
    }
}