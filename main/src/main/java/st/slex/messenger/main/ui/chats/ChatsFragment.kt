package st.slex.messenger.main.ui.chats

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import st.slex.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentChatsBinding
import st.slex.messenger.main.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.main.ui.MainActivity
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsFragment : BaseFragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding: FragmentChatsBinding
        get() = checkNotNull(_binding)

    private var _headerBinding: NavigationDrawerHeaderBinding? = null
    private val headerBinding: NavigationDrawerHeaderBinding
        get() = checkNotNull(_headerBinding)

    private lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
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

    @Inject
    fun injection(viewModelFactory: Lazy<ViewModelProvider.Factory>) {
        this.viewModelFactory = viewModelFactory
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).activityComponent.inject(this)
        super.onAttach(context)
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
            viewModel.currentUser()
                .collect { ResourceChats.User(it).collector(headerBinding.SHOWPROGRESS) }
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
                viewModel.getChats(page).collect {
                    ResourceChats.Chats(it).collector(binding.SHOWPROGRESS)
                }
            }
        }
    }

    private suspend fun <T> ResourceChats<T>.collector(view: View) {
        when (result) {
            is Resource.Success<*> -> {
                when (this) {
                    is ResourceChats.Chats -> {
                        val convertedResult = (result as Resource.Success<List<Resource<ChatsUI>>>)
                        convertedResult.successResult(view)
                    }
                    is ResourceChats.User -> {
                        val convertedResult = (result as Resource.Success<UserUI>)
                        convertedResult.successResult(view)
                    }
                }
            }
            is Resource.Failure<*> -> result.failureResult(view)
            is Resource.Loading -> showLoading()
        }
    }

    sealed class ResourceChats<out T>(val result: T) {

        class Chats(
            result: Resource<List<Resource<ChatsUI>>>
        ) : ResourceChats<Resource<List<Resource<ChatsUI>>>>(result)

        class User(result: Resource<UserUI>) : ResourceChats<Resource<UserUI>>(result)
    }

    @JvmName("collectorUserUI")
    private suspend fun Resource.Success<UserUI>.successResult(view: View) {
        hideLoading()
        drawResult(data)
    }

    @JvmName("collectorChatsUI")
    private suspend fun Resource.Success<List<Resource<ChatsUI>>>.successResult(view: View) {
        hideLoading()
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
        hideLoading()
        Log.e(TAG, exception.message, exception.cause)
    }

    private fun showLoading() {
        binding.SHOWPROGRESS.show()
    }

    private fun hideLoading() {
        binding.SHOWPROGRESS.hide()
    }

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
