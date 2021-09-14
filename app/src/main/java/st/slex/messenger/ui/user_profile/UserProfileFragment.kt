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
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.result.Response

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser().collect {
                when (it) {
                    is Response.Success -> {
                        binding.profileProgress.visibility = View.GONE
                        glide.makeGlideImage(
                            binding.profileImage,
                            it.value.url,
                            true,
                            false,
                            true
                        )
                        binding.profilePhoneNumber.text = it.value.phone
                        binding.profileUsername.text = it.value.username
                        binding.profileFullName.text = it.value.full_name
                        binding.profileBio.text = it.value.bio

                        binding.profileUsernameCard.setOnClickListener { card ->
                            val directions =
                                UserProfileFragmentDirections.actionNavUserProfileToEditUsernameFragment(
                                    it.value.username
                                )
                            val extras = FragmentNavigatorExtras(card to card.transitionName)
                            findNavController().navigate(directions, extras)
                        }
                    }
                    is Response.Loading -> {
                        binding.profileProgress.visibility = View.VISIBLE
                    }
                    is Response.Failure -> {
                        binding.profileProgress.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}