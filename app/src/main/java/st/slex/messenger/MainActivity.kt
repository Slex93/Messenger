package st.slex.messenger

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.ActivityMainBinding
import st.slex.messenger.utilites.appComponent
import st.slex.messenger.utilites.setContacts
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var auth: FirebaseAuth

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    private val viewModel: ActivityViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applicationContext.appComponent.inject(this)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (auth.currentUser != null) {
            navGraph.startDestination = R.id.nav_home
            CoroutineScope(Dispatchers.IO).launch {
                this@MainActivity.setContacts {
                    viewModel.updateContacts(it)
                }
            }
            navController.graph = navGraph
        } else {
            navGraph.startDestination = R.id.nav_enter_phone
            navController.graph = navGraph
        }
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

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) viewModel.statusOnline()
    }

    override fun onStop() {
        super.onStop()
        if (auth.currentUser != null) viewModel.statusOffline()
    }

}


