package st.slex.messenger.single_chat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSingleChatBinding
import st.slex.messenger.contacts.model.Contact
import st.slex.messenger.single_chat.adapter.ChatAdapter
import st.slex.messenger.single_chat.model.ChatRepository
import st.slex.messenger.single_chat.viewmodel.ChatViewModel
import st.slex.messenger.single_chat.viewmodel.ChatViewModelFactory


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var chatUserId: String

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var countMessage = 10
    private var isScrolling = false
    private var isScrollToPosition = true
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val repository = ChatRepository()
    private val chatViewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitAnimation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeExtras()
        initNavigationFields()
        setActionBar()
        initStatus()
        initRecyclerView()
        initMessagesSending()
    }

    private fun initMessagesSending() {
        binding.singleChatRecyclerButton.setOnClickListener {
            isScrollToPosition = true
            val message = binding.singleChatRecyclerTextInput.editText?.text.toString()
            if (message.isEmpty()) {
                val snackBar = Snackbar.make(binding.root, "Empty message", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.singleChatRecyclerTextInput
                snackBar.setAction("Ok") {}
                snackBar.show()
            } else {
                chatViewModel.sendMessage(message, chatUserId)
                binding.singleChatRecyclerTextInput.editText?.setText("")
            }
        }
    }

    private fun initStatus() {
        chatViewModel.initStatus(chatUserId)
        chatViewModel.status.observe(viewLifecycleOwner) {
            binding.toolbarInfo.toolbarInfoStatus.text = it
        }
    }

    private fun initRecyclerView() {
        recycler = binding.singleChatRecycler
        adapter = ChatAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.isNestedScrollingEnabled = false
        swipeRefreshLayout = binding.singleChatRefreshLayout
        chatViewModel.initMessage(chatUserId, countMessage)

        chatViewModel.message.observe(viewLifecycleOwner) { message ->
            if (isScrollToPosition) {
                adapter.addItemToBottom(message) {
                    recycler.smoothScrollToPosition(adapter.itemCount)
                }
            } else {
                adapter.addItemToTop(message) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling
                    && dy < 0
                    && layoutManager.findFirstVisibleItemPosition() <= 3
                ) {
                    updateData()
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }

    }

    private fun updateData() {
        isScrollToPosition = false
        isScrolling = false
        countMessage += 10
        chatViewModel.initMessage(chatUserId, countMessage)
    }

    private fun takeExtras() {
        val args: SingleChatFragmentArgs by navArgs()
        val contact: Contact = args.contact
        val key = args.key
        chatUserId = contact.id
        setExtrasInActionBar(key, contact.fullname)

    }

    private fun setExtrasInActionBar(key: String, fullname: String) {
        binding.toolbarInfo.toolbarInfoCardView.transitionName = key
        binding.toolbarInfo.toolbarInfoUsername.text = fullname
    }

    private fun setTransitAnimation() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    private fun setActionBar() {
        val toolbar = binding.chatToolbar
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_contact))
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

}