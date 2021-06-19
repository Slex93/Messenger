package com.st.slex.common.messenger.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.activity.activity_model.ActivityConst
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.AUTH
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.USER
import com.st.slex.common.messenger.activity.activity_model.ActivityRepository
import com.st.slex.common.messenger.activity.activity_model.User
import com.st.slex.common.messenger.activity.activity_view_model.ActivityViewModel
import com.st.slex.common.messenger.activity.activity_view_model.ActivityViewModelFactory
import com.st.slex.common.messenger.databinding.ActivityMainBinding
import com.st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import com.st.slex.common.messenger.utilites.AppValueEventListener
import com.st.slex.common.messenger.utilites.downloadAndSet
import com.st.slex.common.messenger.utilites.restartActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val repository = ActivityRepository()
    private val activityViewModel: ActivityViewModel by viewModels {
        ActivityViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityViewModel.initFirebase()
        setSupportActionBar(binding.mainActivityToolbar)
        initNavigationFields()
        checkAuth()
    }

    private fun initNavigationFields() {
        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

    private fun checkAuth() {
        if (AUTH.currentUser != null) {
            Log.i("UserMainPre", USER.toString())
            //activityViewModel.initUser()
            initUserNew()
            initNavController()
            Log.i("UserMainPost", USER.toString())
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            navGraph.startDestination = R.id.nav_home
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            navGraph.startDestination = R.id.nav_enter_phone
        }
        navController.graph = navGraph
    }

    private fun initNavController() {
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        initDrawerHeader()
    }

    private fun initDrawerHeader() {
        listenItemClick()
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        headerBinding.navigationHeaderImage.downloadAndSet(USER.url)
        headerBinding.navigationHeaderUserName.text = USER.username
        headerBinding.navigationHeaderPhoneNumber.text = USER.phone

    }
    fun initUserNew() {
        ActivityConst.REF_DATABASE_ROOT.child(ActivityConst.NODE_USER).child(ActivityConst.CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                Log.i("AppValueEventListener", it.toString())
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty()) {
                    USER = User(ActivityConst.CURRENT_UID, USER.phone, USER.username, USER.url)
                    ActivityConst.REF_DATABASE_ROOT.child(ActivityConst.NODE_USER).child(
                        ActivityConst.CURRENT_UID
                    ).child(ActivityConst.CHILD_USERNAME)
                        .setValue(ActivityConst.CURRENT_UID)
                }
            })
    }

    private fun listenItemClick() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_nav_btn_sign_out -> {
                    activityViewModel.signOut()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    this.restartActivity()
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
