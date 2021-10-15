package st.slex.messenger.ui.chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSingleChatBinding
import st.slex.messenger.core.Resource
import st.slex.messenger.data.chat.MessageModel
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.utilites.NODE_CHAT


@ExperimentalCoroutinesApi
class SingleChatFragment : BaseFragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SingleChatViewModel by viewModels {
        viewModelFactory.get()
    }

    private var _uid: String? = null
    private val uid: String get() = _uid ?: ""

    private val adapter: SingleChatAdapter by lazy {
        val baseQuery: Query = FirebaseDatabase
            .getInstance()
            .reference
            .child(NODE_CHAT)
            .child(Firebase.auth.uid.toString())
            .child(uid)
        val options = FirebaseRecyclerOptions.Builder<MessageModel>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(baseQuery, MessageModel::class.java)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUser(uid).collect {
                it.collect()
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.singleChatRecycler.layoutManager = layoutManager
        binding.singleChatRecycler.adapter = adapter
    }

    private fun takeExtras() {
        val args: SingleChatFragmentArgs by navArgs()
        val id = args.id
        _uid = id
        binding.toolbarInfo.toolbarInfoCardView.transitionName = uid
        glide.setImage(
            binding.toolbarInfo.shapeableImageView,
            args.url,
            needCircleCrop = true,
            needCrop = true
        )
    }

    private fun Resource<UserUI>.collect() {
        when (this) {
            is Resource.Success -> {
                data.mapChat(
                    userName = binding.toolbarInfo.usernameTextView,
                    stateText = binding.toolbarInfo.stateTextView
                )
                binding.singleChatRecyclerButton.setOnClickListener(data.sendClicker)
            }
            is Resource.Failure -> {
                Log.e("TAG", exception.message, exception.cause)
            }
            is Resource.Loading -> {
                /*Start response*/
            }
        }
    }

    private val UserUI.sendClicker: View.OnClickListener
        get() = View.OnClickListener {
            val message = binding.singleChatRecyclerTextInput.editText?.text.toString()
            if (message.isEmpty()) {
                val snackBar =
                    Snackbar.make(binding.root, "Empty message", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.singleChatRecyclerTextInput
                snackBar.setAction("Ok") {}
                snackBar.show()
            } else {
                viewModel.sendMessage(message, this)
                binding.singleChatRecycler.scrollToPosition(adapter.itemCount)
                binding.singleChatRecyclerTextInput.editText?.setText("")
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}