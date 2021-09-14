package st.slex.messenger.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.databinding.FragmentUserProfileBinding
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.result.Response

@ExperimentalCoroutinesApi
class UserProfileFragment : BaseFragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels { viewModelFactory.get() }

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
                    }
                    is Response.Loading -> {

                    }
                    is Response.Failure -> {

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