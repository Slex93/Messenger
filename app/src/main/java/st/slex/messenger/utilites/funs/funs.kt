package st.slex.messenger.utilites.funs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.messenger.MessengerApplication
import st.slex.messenger.di.component.AppComponent
import java.text.SimpleDateFormat
import java.util.*

fun Activity.start(activity: Activity) {
    val intent = Intent(this, activity.javaClass)
    startActivity(intent)
    finish()
}

fun String.convertToTime(): String {
    val sdfComp = SimpleDateFormat("yyMMdd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val compareCurrentDate = sdfComp.format(Date(toLong())).toInt()
    val compareDate = sdfComp.format(System.currentTimeMillis()).toInt()
    val format = if (compareDate == compareCurrentDate) {
        "HH:mm"
    } else "EEE d"
    val date = SimpleDateFormat(format, Locale.getDefault()).apply {
        TimeZone.getDefault()
    }.format(Date(toLong()))
    return date
}

fun Fragment.setSupportActionBar(toolbar: MaterialToolbar) {
    (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    NavigationUI.setupWithNavController(
        toolbar,
        findNavController(),
        AppBarConfiguration(setOf(R.id.nav_home))
    )
}

fun View.showPrimarySnackBar(it: String) {
    Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
}

