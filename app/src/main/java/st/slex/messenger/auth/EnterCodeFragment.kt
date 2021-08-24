package st.slex.messenger.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialContainerTransform
import st.slex.common.messenger.R
import st.slex.common.messenger.auth.model.AuthRepository
import st.slex.common.messenger.auth.viewmodel.AuthViewModel
import st.slex.common.messenger.auth.viewmodel.AuthViewModelFactory
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.common.messenger.utilites.restartActivity
import st.slex.common.messenger.utilites.showPrimarySnackBar

class EnterCodeFragment : Fragment() {

    private lateinit var binding: FragmentEnterCodeBinding

    private val repository = AuthRepository()
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Enter Code"
    }

    override fun onStart() {
        super.onStart()
        binding.fragmentCodeTextInput.editText?.addTextChangedListener {
            if (it?.length == 6) {
                binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
                authViewModel.callbackReturnStatus.observe(viewLifecycleOwner) {
                    when (it) {
                        "success" -> {
                            binding.root.showPrimarySnackBar(it)
                            requireActivity().restartActivity()
                        }
                        else -> {
                            binding.root.showPrimarySnackBar(it)
                        }
                    }
                }
                authViewModel.postCode(it.toString())

            }
        }
    }
}