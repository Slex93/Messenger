package st.slex.messenger.ui.user_profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEditUsernameBinding
import st.slex.messenger.ui.core.BaseFragment

@ExperimentalCoroutinesApi
class EditUsernameFragment : BaseFragment() {

    private var _binding: FragmentEditUsernameBinding? = null
    private val binding get() = _binding!!

    val viewModel: UserViewModel by viewModels { viewModelFactory.get() }

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
        _binding = FragmentEditUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: EditUsernameFragmentArgs by navArgs()
        binding.editUnTextInput.editText?.setText(args.username)
        binding.editUnBtnSave.setOnClickListener {
            val username = binding.editUnTextInput.editText?.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveUsername(username.lowercase()).collect {
                    it.collector()
                }
            }
        }
    }

    private fun VoidResponse.collector() {
        when (this) {
            is VoidResponse.Success -> {
                binding.editUnProgress.visibility = View.GONE
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                findNavController().popBackStack()
            }
            is VoidResponse.Failure -> {
                binding.editUnProgress.visibility = View.GONE
                binding.root.showPrimarySnackBar(exception.message.toString())
            }
            is VoidResponse.Loading -> {
                binding.editUnProgress.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
