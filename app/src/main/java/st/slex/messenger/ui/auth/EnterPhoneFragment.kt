package st.slex.messenger.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse

@ExperimentalCoroutinesApi
class EnterPhoneFragment : BaseFragment() {

    private var _binding: FragmentEnterPhoneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Phone Number"
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.fragmentPhoneInput.editText?.addTextChangedListener {
            binding.fragmentPhoneFab.isEnabled = it?.length == 12
        }
        val auth = Firebase.auth
        binding.fragmentPhoneFab.setOnClickListener {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            val phone = binding.fragmentPhoneInput.editText?.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.authPhone(requireActivity(), phone).collect {
                    it.collector()
                }
            }
        }
    }

    private fun AuthResponse.collector() = when (this) {
        is AuthResponse.Success -> {
            binding.root.showPrimarySnackBar(getString(R.string.snack_success))
            authUser()
        }
        is AuthResponse.Send -> {
            binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
            val direction =
                EnterPhoneFragmentDirections.actionNavAuthPhoneToNavAuthCode(id)
            val extras =
                FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
            findNavController().navigate(direction, extras)
        }
        is AuthResponse.Failure -> {
            binding.root.showPrimarySnackBar(exception.toString())
        }
        is AuthResponse.Loading -> {
        }
    }

    private fun authUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authUser().collect { auth ->
                when (auth) {
                    is VoidResponse.Success -> {
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                    is VoidResponse.Failure -> {
                        Log.w(ContentValues.TAG, auth.exception)
                    }
                    is VoidResponse.Loading -> {

                    }
                }
            }
        }
    }


}
