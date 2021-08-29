package st.slex.messenger.utilites

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import st.slex.common.messenger.R
import st.slex.messenger.MainActivity
import st.slex.messenger.MessengerApplication
import st.slex.messenger.di.component.AppComponent
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T> DataSnapshot.getThisValue(): T = getValue(T::class.java) as T

val Context.appComponent: AppComponent
    get() = when (this) {
        is MessengerApplication -> appComponent
        else -> this.applicationContext.appComponent
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

fun Activity.restartActivity() {
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
    this.finish()
}

fun ImageView.downloadAndSet(url: String) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_default_photo))
        .into(this)
}

fun DrawerLayout.lockDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}

fun DrawerLayout.unlockDrawer() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
}

fun String.asTime(): CharSequence? {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}
