package st.slex.messenger.ui.chats

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentChatsBinding
import st.slex.common.messenger.databinding.NavigationDrawerHeaderBinding
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.utilites.NODE_CHATS

@ExperimentalCoroutinesApi
class ChatsFragment : BaseFragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatsViewModel by viewModels { viewModelFactory.get() }

    private val parser: SnapshotParser<ChatsUI> = SnapshotParser {
        return@SnapshotParser it.getValue(ChatsUI.Base::class.java)!!
    }

    private val adapter: ChatsAdapter by lazy {
        val query: Query = FirebaseDatabase
            .getInstance().reference
            .child(NODE_CHATS)
            .child(Firebase.auth.uid.toString())
            .orderByKey()
        val options = FirebaseRecyclerOptions.Builder<ChatsUI>()
            .setLifecycleOwner(this)
            .setQuery(query, parser)
            .build()
        ChatsAdapter(
            options,
            ItemClick(),
            viewModel::getChatUIHead,
            lifecycleScope
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
        binding.recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser().collect { it.collector() }
        }
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        )
        binding.navView.setupWithNavController(findNavController())
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

    private inner class ItemClick : ClickListener<ChatsUI> {
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