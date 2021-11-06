package st.slex.messenger.main.ui.chats

import android.content.ContentValues.TAG
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentChatsBinding
import st.slex.messenger.main.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.core.UIExtensions.changeVisibility
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
        get() = _pageNumber.asStateFlow().stateIn(
            scope = viewLifecycleOwner.lifecycleScope,
            started = SharingStarted.Lazily,
            initialValue = INITIAL_PAGE
        )

    private val adapter: ChatsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChatsAdapter(ChatsItemClicker())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        setupNavigationView()
        return binding.root
    }

    private fun setupNavigationView() = with(binding) {
        val topLvlDestIds: Set<Int> = setOf(R.id.nav_home)
        val appBarConfig = AppBarConfiguration(topLvlDestIds, mainScreenDrawerLayout)
        NavigationUI.setupWithNavController(mainScreenToolbar, findNavController(), appBarConfig)
        navView.setupWithNavController(findNavController())
        val headerView = navView.getHeaderView(0)
        _headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentUser().collect { it.collector() }
        }
        chatsJob.start()
        binding.recyclerView.adapter = adapter
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private val chatsJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            pageNumber.collect { page ->
                viewModel.getChats(page).collect { it.collector() }
            }
        }
    }

    @JvmName("collectorChatsUI")
    private suspend fun Resource<List<Resource<ChatsUI>>>.collector(): Unit = when (this) {
        is Resource.Success -> successResult()
        is Resource.Failure -> failureResult(binding.SHOWPROGRESS)
        is Resource.Loading -> loadingResult()
    }

    @JvmName("collectorUserUI")
    private suspend fun Resource<UserUI>.collector(): Unit = when (this) {
        is Resource.Success -> successResult()
        is Resource.Failure -> failureResult(headerBinding.SHOWPROGRESS)
        is Resource.Loading -> loadingResult()
    }

    @JvmName("collectorUserUI")
    private suspend fun Resource.Success<UserUI>.successResult() {
        headerBinding.SHOWPROGRESS.changeVisibility()
        drawResult(data)
    }

    @JvmName("collectorChatsUI")
    private suspend fun Resource.Success<List<Resource<ChatsUI>>>.successResult() {
        binding.SHOWPROGRESS.changeVisibility()
        drawResult(data)
    }

    private suspend fun drawResult(result: UserUI) = withContext(Dispatchers.Main) {
        result.mapMainScreen(
            phoneNumber = headerBinding.phoneTextView,
            userName = headerBinding.usernameTextView,
            avatar = headerBinding.avatarImageView
        )
    }

    private suspend fun drawResult(result: List<Resource<ChatsUI>>) =
        withContext(Dispatchers.Main) {
            adapter.setItems(result)
        }

    private suspend fun <T> Resource.Failure<T>.failureResult(view: View) {
        view.changeVisibility()
        Log.e(TAG, exception.message, exception.cause)
    }

    private suspend fun loadingResult() = headerBinding.SHOWPROGRESS.changeVisibility()

    override fun onDestroyView() {
        super.onDestroyView()
        cleanAllJobsAndVariables()
    }

    private fun cleanAllJobsAndVariables() {
        _binding = null
        _headerBinding = null
        _pageNumber.tryEmit(INITIAL_PAGE)
        chatsJob.cancel(CAUSE_DESTROY)
    }

    companion object {
        private const val CAUSE_DESTROY = "onDestroyView"
        private const val INITIAL_PAGE = 1
    }
}
