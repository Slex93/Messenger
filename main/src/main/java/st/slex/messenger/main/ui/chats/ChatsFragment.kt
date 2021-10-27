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
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.core.FirebaseConstants.NODE_CHATS
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

    private val parser: SnapshotParser<ChatsUI> = SnapshotParser {
        return@SnapshotParser it.getValue(ChatsUI.Base::class.java)!!
    }

    private var _adapter: ChatsAdapter? = null
    private val adapter: ChatsAdapter
        get() = checkNotNull(_adapter)

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
        val query: Query = FirebaseDatabase
            .getInstance().reference
            .child(NODE_CHATS)
            .child(Firebase.auth.uid.toString())
            .orderByKey()
        val options = FirebaseRecyclerOptions.Builder<ChatsUI>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(query, parser)
            .build()
        _adapter = ChatsAdapter(
            options,
            ChatsItemClicker(),
            viewModel::getChatUIHead,
            viewLifecycleOwner.lifecycleScope
        )
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.currentUser().collect {
                launch(Dispatchers.Main) { it.collector() }
            }
        }
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        )
        binding.navView.setupWithNavController(findNavController())
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        binding.recyclerView.adapter = adapter
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun Resource<UserUI>.collector() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        when (this) {
            is Resource.Success -> {
                data.mapMainScreen(
                    phoneNumber = headerBinding.phoneTextView,
                    userName = headerBinding.usernameTextView,
                    avatar = headerBinding.avatarImageView
                )
            }
            is Resource.Failure -> {
                Log.i("Cancelled", exception.message.toString())
            }
            is Resource.Loading -> {
                /*Start progress bar*/
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}