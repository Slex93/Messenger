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
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentChatsBinding
import st.slex.messenger.main.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.user_profile.UserUI

@ExperimentalCoroutinesApi
class ChatsFragment : BaseFragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding: FragmentChatsBinding get() = checkNotNull(_binding)

    private var _headerBinding: NavigationDrawerHeaderBinding? = null
    private val headerBinding: NavigationDrawerHeaderBinding get() = checkNotNull(_headerBinding)

    private val viewModel: ChatsViewModel by viewModels { viewModelFactory.get() }
    private val _pageNumber: MutableStateFlow<Int> = MutableStateFlow(INITIAL_PAGE)
    private val pageNumber: StateFlow<Int>
        get() = _pageNumber.asStateFlow()
            .stateIn(
                scope = viewLifecycleOwner.lifecycleScope,
                started = SharingStarted.Lazily,
                initialValue = INITIAL_PAGE
            )

    private val adapter: ChatsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChatsAdapter(ChatsItemClicker())
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
        val headerView = binding.navView.getHeaderView(0)
        _headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        )
        binding.navView.setupWithNavController(findNavController())
        userHeadJob.start()
        chatsJob.start()
        binding.recyclerView.adapter = adapter
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private val userHeadJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.currentUser().collect { userCollector(it) }
        }
    }

    private val chatsJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            pageNumber.collect { page ->
                viewModel.getChats(page * PAGE_SIZE).collect {
                    launch(Dispatchers.Main) { chatsCollector(it) }
                }
            }
        }
    }

    private fun chatsCollector(result: Resource<List<Resource<ChatsUI>>>) {
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

    private suspend fun userCollector(result: Resource<UserUI>) {
        when (result) {
            is Resource.Success -> userCollector(result.data)
            is Resource.Failure -> Log.i("Cancelled", result.exception.message.toString())
            is Resource.Loading -> {
                /*Start progress bar*/
            }
        }
    }

    private suspend fun userCollector(user: UserUI) = withContext(Dispatchers.Main) {
        user.mapMainScreen(
            phoneNumber = headerBinding.phoneTextView,
            userName = headerBinding.usernameTextView,
            avatar = headerBinding.avatarImageView
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
        _pageNumber.tryEmit(INITIAL_PAGE)
        chatsJob.cancel()
    }

    companion object {
        private const val CAUSE_DESTROY = "onDestroyView"
        private const val PAGE_SIZE = 10
        private const val INITIAL_PAGE = 1
    }
}
