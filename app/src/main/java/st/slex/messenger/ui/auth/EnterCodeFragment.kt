package st.slex.messenger.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse

@ExperimentalCoroutinesApi
class EnterCodeFragment : BaseFragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!

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
        binding.fragmentCodeTextInput.editText?.addTextChangedListener { code ->
            code.toString().apply {
                if (length == 6) textListener(args.id)
            }
        }
    }

    private fun String.textListener(id: String) {
        binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.sendCode(id = id, code = this@textListener).collect {
                when (it) {
                    is AuthResponse.Success -> {
                        binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                        this.async {
                            authViewModel.authUser().collect { auth ->
                                when (auth) {
                                    is VoidResponse.Success -> {
                                        requireActivity().recreate()
                                    }
                                    is VoidResponse.Failure -> {
                                        Log.e(
                                            "EnterCode: AuthResponse: Auth",
                                            auth.exception.message,
                                            auth.exception.cause
                                        )
                                    }
                                    is VoidResponse.Loading -> {
                                    }
                                }
                            }
                        }
                    }
                    is AuthResponse.Failure -> {
                        Log.e("EnterCode: AuthResponse", it.exception.message, it.exception.cause)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private val authObserver: Observer<VoidResponse> = Observer {
        if (it is VoidResponse.Failure) Log.e("$this", it.exception.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}