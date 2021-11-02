package st.slex.messenger.main.ui.chats

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentChatsBinding
import st.slex.messenger.main.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.user_profile.UserUI

@ExperimentalCoroutinesApi
class ChatsFragment : BaseFragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatsViewModel by viewModels { viewModelFactory.get() }

    private val adapter: ChatsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChatsAdapter(
            ChatsItemClicker(),
            viewModel::getChatUIHead,
            viewLifecycleOwner.lifecycleScope
        )
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
        currentUserJob.start()
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        )
        binding.recyclerView.adapter = adapter
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        chatsJob.start()
        binding.navView.setupWithNavController(findNavController())
    }

    private val currentUserJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO, start = CoroutineStart.LAZY
        ) {
            viewModel.currentUser().collect {
                launch(Dispatchers.Main) { userCollector(it) }
            }
        }
    }

    private val chatsJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO, start = CoroutineStart.LAZY
        ) {
            viewModel.getChats().collect {
                launch(Dispatchers.Main) { chatsCollector(it) }
            }
        }
    }

    private fun chatsCollector(result: Resource<List<ChatsUI>>) {
        when (result) {
            is Resource.Success -> adapter.setItems(result.data)
            is Resource.Failure -> {
                Log.i("Cancelled", result.exception.message.toString())
            }
            is Resource.Loading -> {
                /*Start progress bar*/
            }
        }
    }

    private fun userCollector(result: Resource<UserUI>) {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        when (result) {
            is Resource.Success -> {
                result.data.mapMainScreen(
                    phoneNumber = headerBinding.phoneTextView,
                    userName = headerBinding.usernameTextView,
                    avatar = headerBinding.avatarImageView
                )
            }
            is Resource.Failure -> {
                Log.i("Cancelled", result.exception.message.toString())
            }
            is Resource.Loading -> {
                /*Start progress bar*/
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentUserJob.cancel(CAUSE_DESTROY)
        chatsJob.cancel(CAUSE_DESTROY)
    }

    companion object {
        private const val CAUSE_DESTROY = "onDestroyView"
    }
}