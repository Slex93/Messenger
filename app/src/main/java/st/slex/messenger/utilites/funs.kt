package st.slex.messenger.utilites

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import st.slex.common.messenger.R
import st.slex.messenger.MainActivity
import java.text.SimpleDateFormat
import java.util.*

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
