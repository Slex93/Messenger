package st.slex.messenger.main.ui.main

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.ActivityMainBinding
import st.slex.messenger.main.di.component.DaggerMainActivityComponent
import st.slex.messenger.main.di.component.MainActivityComponent
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _activityComponent: MainActivityComponent? = null
    val activityComponent: MainActivityComponent
        get() = checkNotNull(_activityComponent)

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding)

    private val viewModel: ActivityViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        _activityComponent = DaggerMainActivityComponent.builder().activity(this).create()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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


