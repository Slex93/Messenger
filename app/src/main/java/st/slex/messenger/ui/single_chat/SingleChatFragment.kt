package st.slex.messenger.ui.single_chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSingleChatBinding
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.ui.single_chat.adapter.ChatAdapter
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.result.Response


@ExperimentalCoroutinesApi
class SingleChatFragment : BaseFragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var uid: String

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var countMessage = 10
    private var isScrolling = false
    private var isScrollToPosition = true
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: SingleChatViewModel by viewModels {
        viewModelFactory.get()
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
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeExtras()
        NavigationUI.setupWithNavController(
            binding.chatToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_contact))
        )
        viewModel.getUser(uid).observe(viewLifecycleOwner, userObserver)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler = binding.singleChatRecycler
        adapter = ChatAdapter(auth.currentUser?.uid.toString())
        layoutManager = LinearLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.isNestedScrollingEnabled = false
        swipeRefreshLayout = binding.singleChatRefreshLayout
        viewModel.getMessages(uid, countMessage).observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    if (isScrollToPosition) {
                        adapter.addItemToBottom(it.value) {
                            recycler.smoothScrollToPosition(adapter.itemCount)
                        }
                    } else {
                        adapter.addItemToTop(it.value) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
                is Response.Failure -> {

                }
                is Response.Loading -> {

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
    }

    private fun takeExtras() {
        val args: SingleChatFragmentArgs by navArgs()
        val id = args.id
        uid = id
        binding.toolbarInfo.toolbarInfoCardView.transitionName = uid
    }

    private val userObserver: Observer<Response<UserModel>> = Observer { user ->
        when (user) {
            is Response.Success -> {
                binding.toolbarInfo.toolbarInfoUsername.text = user.value.full_name
                binding.toolbarInfo.toolbarInfoStatus.text = user.value.state
                binding.singleChatRecyclerButton.setOnClickListener {
                    isScrollToPosition = true
                    val message = binding.singleChatRecyclerTextInput.editText?.text.toString()
                    if (message.isEmpty()) {
                        val snackBar =
                            Snackbar.make(binding.root, "Empty message", Snackbar.LENGTH_SHORT)
                        snackBar.anchorView = binding.singleChatRecyclerTextInput
                        snackBar.setAction("Ok") {}
                        snackBar.show()
                    } else {
                        viewModel.sendMessage(message, user.value)
                        binding.singleChatRecyclerTextInput.editText?.setText("")
                    }
                }
            }
            is Response.Failure -> {
                Log.e("$this", user.exception.toString())
            }
        }
    }

    private fun setTransitAnimation() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}