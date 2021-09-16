package st.slex.messenger.ui.chats

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
import st.slex.common.messenger.databinding.FragmentChatsBinding
import st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.core.Response
import st.slex.messenger.ui.chats.adapter.ChatsAdapter
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.utilites.base.GlideBase

@ExperimentalCoroutinesApi
class ChatsFragment : BaseFragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatsAdapter

    private val viewModel: ChatsViewModel by viewModels {
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
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
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

    private fun ChatsUIResult.collect() {
        when (this) {
            is ChatsUIResult.Success -> {
                adapter.addChat(data)
            }
            is ChatsUIResult.Failure -> {
                Log.e(
                    "Exception im MainList from the flow",
                    exception.message.toString(),
                    exception.cause
                )
            }
            is ChatsUIResult.Loading -> {

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
        adapter = ChatsAdapter(OpenChat())
        postponeEnterTransition()
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private inner class OpenChat : ClickListener<ChatsUI> {
        override fun click(item: ChatsUI) {
            item.startChat { card, url ->
                val directions =
                    ChatsFragmentDirections.actionNavHomeToNavSingleChat(
                        card.transitionName,
                        url
                    )
                val extras = FragmentNavigatorExtras(card to card.transitionName)
                findNavController().navigate(directions, extras)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}