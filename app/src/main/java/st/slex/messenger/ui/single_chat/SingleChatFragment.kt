package st.slex.messenger.ui.single_chat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSingleChatBinding
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.utilites.NODE_MESSAGES


@ExperimentalCoroutinesApi
class SingleChatFragment : BaseFragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!
    private val args: SingleChatFragmentArgs by navArgs()
    private val viewModel: SingleChatViewModel by viewModels { viewModelFactory.get() }

    private val adapter: SingleChatAdapter by lazy {
        val baseQuery: Query = FirebaseDatabase
            .getInstance()
            .reference
            .child(NODE_MESSAGES)
            .child(Firebase.auth.uid.toString())
            .child(args.id)
        val options = FirebaseRecyclerOptions.Builder<MessageUI>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(baseQuery) {
                it.getValue(MessageUI.Base::class.java)!!
            }
            .build()
        SingleChatAdapter(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    private val bindHeadToolbar by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO,
            start = CoroutineStart.LAZY
        ) {
            viewModel.getChatUIHead(args.id).collect {
                if (it is Resource.Success) {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        with(binding.toolbarInfo) { it.data.bind(stateTextView, usernameTextView) }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        binding.toolbarInfo.toolbarInfoCardView.transitionName = args.id
        viewLifecycleOwner.lifecycleScope.launchWhenStarted { setImage() }
        bindHeadToolbar.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NavigationUI.setupWithNavController(
            binding.chatToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_contact))
        )
        binding.singleChatRecyclerButton.setOnClickListener(sendClicker)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.singleChatRecycler.layoutManager = layoutManager
        binding.singleChatRecycler.adapter = adapter
    }

    private fun setImage() = glide.setImage(
        imageView = binding.toolbarInfo.shapeableImageView,
        url = args.url,
        needCircleCrop = true,
        needCrop = true,
        needOriginal = false
    )

    private val sendClicker: View.OnClickListener
        get() = View.OnClickListener {
            val message = binding.singleChatRecyclerTextInput.editText?.text.toString()
            if (message.isEmpty()) {
                val snackBar =
                    Snackbar.make(binding.root, "Empty message", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.singleChatRecyclerTextInput
                snackBar.setAction("Ok") {}
                snackBar.show()
            } else {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.sendMessage(receiverId = args.id, message = message).collect {
                        collect(it)
                    }
                }
            }
        }

    private suspend fun collect(result: Resource<Nothing?>) = withContext(Dispatchers.Main) {
        if (result is Resource.Success) {
            binding.singleChatRecycler.scrollToPosition(adapter.itemCount)
            binding.singleChatRecyclerTextInput.editText?.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}