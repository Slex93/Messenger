package st.slex.messenger

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.ActivityMainBinding
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.impl.ActivityRepositoryImpl
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.checkPermission


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    private val repository = ActivityRepositoryImpl()
    private val viewModel: ActivityViewModel by viewModels {
        ActivityViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.initFirebase()
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        checkAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkAuth() = try {
        if (AUTH.currentUser?.uid.isNullOrEmpty()) {
            navGraph.startDestination = R.id.nav_enter_phone
        }
        navController.graph = navGraph
    } catch (e: Exception) {
        Log.i("MainActivity:", e.toString())
        navGraph.startDestination = R.id.nav_enter_phone
        navController.graph = navGraph
    }

    @SuppressLint("Range")
    private fun getContacts(): List<ContactModel> {
        val contactList = mutableListOf<ContactModel>()
        if (this.checkPermission(READ_CONTACTS)) {
            val cursor = this.contentResolver.query(
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
                    val newModel = ContactModel(fullname = fullName, phone = setPhone)
                    contactList.add(newModel)
                }
            }
            cursor?.close()
        }
        return contactList
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
            val contacts = getContacts()
            viewModel.updatePhoneToDatabase(contacts)
        }
    }

    override fun onStart() {
        super.onStart()
        if (AUTH.currentUser != null) viewModel.statusOnline()
    }

    override fun onStop() {
        super.onStop()
        if (AUTH.currentUser != null) viewModel.statusOffline()
    }

}


