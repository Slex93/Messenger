package st.slex.messenger.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentMainBinding
import st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.ui.main_screen.adapter.MainAdapter
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.result.Response

@ExperimentalCoroutinesApi
class MainFragment : BaseFragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    private val viewModel: MainScreenViewModel by viewModels {
        viewModelFactory.get()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getChatList().collect {
                when (it) {
                    is Response.Success -> {
                        adapter.addChat(it.value)
                    }
                    is Response.Failure -> {
                        Log.e(
                            "Exception im MainList from the flow",
                            it.exception.message.toString(),
                            it.exception.cause
                        )
                    }
                    is Response.Loading -> {

                    }
                }
            }
        }
    }

    private fun setUserInfoInHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        viewModel.currentUser.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    //headerBinding.navigationHeaderImage.downloadAndSet(it.value.url)
                    headerBinding.navigationHeaderUserName.text = it.value.username
                    headerBinding.navigationHeaderPhoneNumber.text = it.value.phone
                }
                is Response.Failure -> {
                    Log.i("Cancelled", it.exception.message.toString())
                }
                is Response.Loading -> {
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter(clickListener)
        postponeEnterTransition()
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private val clickListener = CardClickListener { card ->
        val directions = MainFragmentDirections.actionNavHomeToNavSingleChat(card.transitionName)
        val extras = FragmentNavigatorExtras(card to card.transitionName)
        findNavController().navigate(directions, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}