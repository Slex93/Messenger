package st.slex.messenger.ui.main

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.ActivityMainBinding
import st.slex.messenger.appComponent
import st.slex.messenger.di.component.MainActivitySubcomponent
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var manager: Lazy<ContactsManager>

    private var _activityComponent: MainActivitySubcomponent? = null
    val activityComponent: MainActivitySubcomponent
        get() = checkNotNull(_activityComponent)

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding)

    private val viewModel: ActivityViewModel by viewModels { viewModelFactory }

    private val contactsJob: Job by lazy {
        lifecycleScope.launchWhenCreated {
            manager.get().setContacts().collect {
                launch { viewModel.updateContacts(it).collect() }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _activityComponent = appComponent.mainActivityBuilder.activity(this).create()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        contactsJob.start()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        viewModel.changeState(getString(R.string.state_online))
    }

    override fun onStop() {
        super.onStop()
        viewModel.changeState(getString(R.string.state_offline))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        contactsJob.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            contactsJob.start()
        }
    }
}


