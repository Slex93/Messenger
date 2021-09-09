package st.slex.messenger.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse

@ExperimentalCoroutinesApi
class EnterCodeFragment : BaseFragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!
    private var _id: String? = null
    private val id get() = _id!!

    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_code)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: EnterCodeFragmentArgs by navArgs()
        _id = args.id
    }

    override fun onResume() {
        super.onResume()
        binding.fragmentCodeTextInput.editText?.addTextChangedListener()
        binding.fragmentCodeTextInput.editText?.addTextChangedListener { code ->
            if (code?.length == 6) {
                binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    authViewModel.sendCode(id = id, code = code.toString()).collect {
                        it.collector()
                    }
                }
            }
        }
    }

    private fun AuthResponse.collector() {
        when (this) {
            is AuthResponse.Success -> {
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                viewLifecycleOwner.lifecycleScope.launch {
                    authViewModel.authUser().collect {
                        collector(it)
                    }
                }
            }
            is AuthResponse.Failure -> {
                Log.e("EnterCode: AuthResponse", exception.message, exception.cause)
            }
            else -> {
                Log.e("EnterCode: Loading", "loading...")
            }
        }
    }

    private fun collector(response: VoidResponse) {
        when (response) {
            is VoidResponse.Success -> {
                requireActivity().startActivity(
                    Intent(requireContext(), MainActivity::class.java)
                )
                requireActivity().finish()
            }
            is VoidResponse.Failure -> {
                Log.e(
                    "EnterCode: AuthResponse: Auth",
                    response.exception.message,
                    response.exception.cause
                )
            }
            is VoidResponse.Loading -> {

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}