package st.slex.messenger.ui.main_screen

import android.graphics.Color
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
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentMainBinding
import st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.core.Response
import st.slex.messenger.core.TestResponse
import st.slex.messenger.ui.main_screen.adapter.MainAdapter
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.base.GlideBase

@ExperimentalCoroutinesApi
class MainFragment : BaseFragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    private val viewModel: MainScreenViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
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
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getChats(10).collect {
                it.collect()
            }
        }
    }

    private fun TestResponse<List<ChatsUI>>.collect() {
        when (this) {
            is TestResponse.Success -> {
                adapter.addChat(value)
            }
            is TestResponse.Failure -> {
                Log.e(
                    "Exception im MainList from the flow",
                    exception.message.toString(),
                    exception.cause
                )
            }
            is TestResponse.Loading -> {

            }
        }
    }

    private fun setUserInfoInHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser().collect {
                when (it) {
                    is Response.Success -> {
                        GlideBase {}.setImageWithRequest(
                            headerBinding.navigationHeaderImage,
                            it.value.url,
                            needCrop = true
                        )
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

    }

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter(clickListener, glide)
        postponeEnterTransition()
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private val clickListener = CardClickListener { card, url ->
        val directions =
            MainFragmentDirections.actionNavHomeToNavSingleChat(card.transitionName, url)
        val extras = FragmentNavigatorExtras(card to card.transitionName)
        findNavController().navigate(directions, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}