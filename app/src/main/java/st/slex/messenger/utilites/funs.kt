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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.common.messenger.R
import st.slex.messenger.MainActivity
import st.slex.messenger.MessengerApplication
import st.slex.messenger.di.component.AppComponent
import st.slex.messenger.utilites.result.EventResponse
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
suspend fun DatabaseReference.valueEventFlow(): Flow<EventResponse> = callbackFlow {
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            trySendBlocking(EventResponse.Success(snapshot)).isSuccess
        }

        override fun onCancelled(error: DatabaseError) {
            trySendBlocking(EventResponse.Cancelled(error)).isFailure
        }
    }
    addValueEventListener(valueEventListener)
    awaitClose {
        removeEventListener(valueEventListener)
    }
}

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
