package st.slex.messenger.ui.activities

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.ActivityMainBinding
import st.slex.messenger.utilites.funs.appComponent
import st.slex.messenger.utilites.funs.setContacts
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var auth: FirebaseAuth

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private val viewModel: ActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applicationContext.appComponent.inject(this)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (auth.currentUser != null) {
            CoroutineScope(Dispatchers.IO).launch {
                this@MainActivity.setContacts {
                    viewModel.updateContacts(it)
                }
            }
            navGraph.startDestination = R.id.nav_home
            navController.graph = navGraph
        } else {
            navGraph.startDestination = R.id.nav_enter_phone
            navController.graph = navGraph
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) viewModel.changeState(getString(R.string.state_online))
    }

    override fun onStop() {
        super.onStop()
        if (auth.currentUser != null) viewModel.changeState(getString(R.string.state_offline))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
            CoroutineScope(Dispatchers.IO).launch {
                this@MainActivity.setContacts {
                    viewModel.updateContacts(it)
                }
            }
        }
    }

}


