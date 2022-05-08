package st.slex.messenger.main.ui

import android.Manifest.permission.READ_CONTACTS
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.core.FirebaseConstants.CHILD_ID
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.ActivityMainBinding
import st.slex.messenger.main.di.component.DaggerMainActivityComponent
import st.slex.messenger.main.di.component.MainActivityComponent
import st.slex.messenger.main.notification.PushService.Companion.ACTION_SHOW_MESSAGE
import st.slex.messenger.main.notification.PushService.Companion.INTENT_FILTER
import st.slex.messenger.main.notification.PushService.Companion.KEY_ACTION
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val activityComponent: MainActivityComponent by lazy {
        DaggerMainActivityComponent.builder().activity(this).create()
    }

    private val pushBroadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val extras = intent?.extras
                Log.i(TAG, extras.toString())
                Toast.makeText(context, extras.toString(), Toast.LENGTH_SHORT).show()
                extras?.keySet()?.firstOrNull { it == KEY_ACTION }?.let { key ->
                    when (extras.getString(key)) {
                        ACTION_SHOW_MESSAGE -> {
                            extras.getString(CHILD_ID)?.let { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> Log.e(TAG, "No needed key found")
                    }
                }
            }
        }
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding)

    private val viewModel: ActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_FILTER)
        registerReceiver(pushBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        viewModel.contactsJob.start()
        viewModel.changeState(getString(R.string.state_online))
    }

    override fun onStop() {
        super.onStop()
        viewModel.changeState(getString(R.string.state_offline))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.contactsJob.cancel()
        unregisterReceiver(pushBroadcastReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(this, READ_CONTACTS) == PERMISSION_GRANTED) {
            viewModel.contactsJob.start()
        }
    }
}


