package st.slex.messenger.main.utilites.funs

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import st.slex.messenger.main.R
import java.text.SimpleDateFormat
import java.util.*

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

