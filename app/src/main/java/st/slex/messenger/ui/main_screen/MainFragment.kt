package st.slex.messenger.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentMainBinding
import st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.repository.impl.MainRepositoryImpl
import st.slex.messenger.ui.main_screen.adapter.MainAdapter
import st.slex.messenger.ui.main_screen.viewmodel.MainScreenViewModel
import st.slex.messenger.ui.main_screen.viewmodel.MainScreenViewModelFactory
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.downloadAndSet
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.result.Resource

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val repository by lazy { MainRepositoryImpl() }

    private val viewModel: MainScreenViewModel by viewModels {
        MainScreenViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfoInHeader()
        initNavigationFields()
        setActionBar()
        initRecyclerView()
    }

    private fun setActionBar() {
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            navController,
            appBarConfiguration
        )
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_nav_btn_sign_out -> {
                    AUTH.signOut()
                    requireActivity().restartActivity()
                }
            }
            false
        }
    }

    @ExperimentalCoroutinesApi
    private fun setUserInfoInHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        viewModel.currentUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    headerBinding.navigationHeaderImage.downloadAndSet(it.data.url)
                    headerBinding.navigationHeaderUserName.text = it.data.username
                    headerBinding.navigationHeaderPhoneNumber.text = it.data.phone
                }
                is Resource.Failure -> {
                    Log.i("Cancelled", it.exception.message.toString())
                }
                is Resource.Loading -> {
                    Log.i("Loading", it.toString())
                }
            }
        }
    }

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter()
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        viewModel.mainMessage.observe(viewLifecycleOwner) {
            adapter.makeMainList(it as MutableList<MessageModel>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}