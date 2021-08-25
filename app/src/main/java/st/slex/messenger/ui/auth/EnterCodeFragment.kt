package st.slex.messenger.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.data.repository.impl.AuthRepositoryImpl
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.result.AuthResult
import st.slex.messenger.utilites.showPrimarySnackBar

class EnterCodeFragment : Fragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!

    private val repository = AuthRepositoryImpl()
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
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
        binding.fragmentCodeTextInput.editText?.addTextChangedListener { code ->
            code.toString().apply {
                if (length == 6) textListener(args.id)
            }
        }
    }

    private fun String.textListener(id: String) {
        binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
        authViewModel.authResultModel.observe(viewLifecycleOwner) { it.observer }
        authViewModel.sendCode(id = id, code = this)
    }

    private val AuthResult<AuthUserModel>.observer: Unit
        get() = when (this) {
            is AuthResult.Success -> {
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                authViewModel.authUser(data)
                requireActivity().restartActivity()
            }
            is AuthResult.Failure -> {
                binding.root.showPrimarySnackBar(exception)
            }
            else -> {
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}