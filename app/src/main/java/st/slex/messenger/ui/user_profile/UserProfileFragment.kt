package st.slex.messenger.ui.user_profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentUserProfileBinding
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.utilites.funs.setSupportActionBar

@ExperimentalCoroutinesApi
class UserProfileFragment : BaseFragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels { viewModelFactory.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser().collect {
                it.collector()
            }
        }
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
        setSupportActionBar(binding.userProfileToolbar)
    }

    private fun UserUiResult.collector() {
        when (this) {
            is UserUiResult.Success -> {
                this.data.mapProfile(
                    glide = glide,
                    phoneNumber = binding.container.phoneTextView,
                    userName = binding.container.usernameTextView,
                    avatar = binding.avatarImageView,
                    bioText = binding.container.bioTextView,
                    fullName = binding.container.fullNameTextView,
                    usernameCard = binding.container.usernameCardView
                )

                binding.container.usernameCardView.setOnClickListener {
                    data.changeUsername { card, username ->
                        val directions = UserProfileFragmentDirections
                            .actionNavUserProfileToEditUsernameFragment(username)
                        val extras = FragmentNavigatorExtras(card to card.transitionName)
                        findNavController().navigate(directions, extras)
                    }
                }
            }
            is UserUiResult.Loading -> {
            }
            is UserUiResult.Failure -> {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}