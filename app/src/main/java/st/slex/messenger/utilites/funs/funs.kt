package st.slex.messenger.utilites.funs

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import st.slex.common.messenger.R
import st.slex.messenger.MessengerApplication
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.di.component.AppComponent
import st.slex.messenger.utilites.PERMISSION_REQUEST
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
val Context.appComponent: AppComponent
    get() = when (this) {
        is MessengerApplication -> appComponent
        else -> this.applicationContext.appComponent
    }

fun String.convertToTime(): String {
    val sdfComp = SimpleDateFormat("yyMMdd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val compareCurrentDate = sdfComp.format(Date(toLong())).toInt()
    val compareDate = sdfComp.format(System.currentTimeMillis()).toInt()
    val format = if (compareDate == compareCurrentDate) {
        "HH:mm"
    } else "EEE d MMMM"
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

suspend fun Activity.checkPermission(permission: String): Boolean = withContext(Dispatchers.IO) {
    return@withContext if (ContextCompat.checkSelfPermission(
            this@checkPermission,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this@checkPermission,
            arrayOf(permission),
            PERMISSION_REQUEST
        )
        false
    } else true
}

@SuppressLint("Range")
suspend inline fun Activity.setContacts(crossinline function: (list: List<ContactModel>) -> Unit) =
    withContext(Dispatchers.IO) {
        if (checkPermission(Manifest.permission.READ_CONTACTS)) {
            val list = mutableListOf<ContactModel>()
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            cursor?.let {
                while (it.moveToNext()) {
                    val fullName =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phone =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val setPhone = phone.replace(Regex("[\\s,-]"), "")
                    val newModel = ContactModel(full_name = fullName, phone = setPhone)
                    list.add(newModel)
                }
            }
            cursor?.close()
            function(list)
        }
    }

fun View.showPrimarySnackBar(it: String) {
    Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.downloadAndSet(url: String) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_default_photo))
        .into(this)
}

fun String.asTime(): CharSequence? {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}
