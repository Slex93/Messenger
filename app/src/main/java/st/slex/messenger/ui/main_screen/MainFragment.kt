package st.slex.messenger.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import st.slex.messenger.utilites.downloadAndSet
import st.slex.messenger.utilites.result.Resource

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    private val repository = MainRepositoryImpl()
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
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        )
        binding.navView.setupWithNavController(findNavController())
        initRecyclerView()
    }

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

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewModel.mainMessage.observe(viewLifecycleOwner) {
            adapter.makeMainList(it as MutableList<MessageModel>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}