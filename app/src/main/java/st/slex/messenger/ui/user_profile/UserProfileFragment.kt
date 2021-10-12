package st.slex.messenger.ui.user_profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.utilites.funs.setSupportActionBar

@ExperimentalCoroutinesApi
class UserProfileFragment : BaseFragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels { viewModelFactory.get() }

    private var _imageLauncher: ActivityResultLauncher<Intent>? = null
    private val imageLauncher: ActivityResultLauncher<Intent> get() = _imageLauncher!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _imageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            it.imageCallback()
        }
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setSupportActionBar(binding.userProfileToolbar)
        scope().start()
    }

    private fun scope() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.currentUser().collect {
            it.collector()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.changeImageFab.setOnClickListener(changeImageClickListener)
    }

    private val changeImageClickListener: View.OnClickListener
        get() = View.OnClickListener {
            imageLauncher.launch(
                Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_PICK
                }
            )
        }

    private fun ActivityResult.imageCallback(): Unit? =
        this.data?.data?.let { it ->
            if (it.getRealPath().isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.saveImage(it).collect { result ->
                        result.collector
                    }
                }
            }
        }

    private fun Uri.getRealPath(): String {
        val cursor = requireActivity().contentResolver.query(
            this,
            null,
            null,
            null, null,
            null
        )
        val result = if (cursor == null) {
            this.path.toString()
        } else with(cursor) {
            moveToNext()
            val id = getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            getString(id)
        }
        cursor?.close()
        return result
    }

    private val UIResult<*>.collector: () -> Unit
        get() = {
            when (this) {
                is UIResult.Success -> {
                    scope().cancel()
                    scope().start()
                }
                is UIResult.Failure -> {
                }
                is UIResult.Loading -> {
                }
            }
        }


    private fun UIResult<UserUI>.collector() {
        when (this) {
            is UIResult.Success -> {
                data.mapProfile(
                    glide = glide,
                    phoneNumber = binding.container.phoneTextView,
                    userName = binding.container.usernameTextView,
                    avatar = binding.avatarImageView,
                    bioText = binding.container.bioTextView,
                    fullName = binding.container.fullNameTextView,
                    usernameCard = binding.container.usernameCardView,
                    toolbar = binding.userProfileToolbar
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
            is UIResult.Loading -> {
            }
            is UIResult.Failure -> {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

