package st.slex.messenger.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.data.repository.impl.AuthRepositoryImpl
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.result.AuthResult
import st.slex.messenger.utilites.showPrimarySnackBar

class EnterPhoneFragment : Fragment() {

    private var _binding: FragmentEnterPhoneBinding? = null
    private val binding: FragmentEnterPhoneBinding get() = _binding!!

    private val repository = AuthRepositoryImpl()
    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.authResultModel.removeObservers(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Phone Number"
        (binding.signInCountryCodeLayout.editText as AutoCompleteTextView)
            .setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.sign_in_list_item,
                    listOf("+7", "+8", "+9", "+10")
                )
            )
        binding.fragmentPhoneInput.editText?.addTextChangedListener {
            binding.fragmentPhoneFab.isEnabled = it?.length == 10
        }
        binding.fragmentPhoneFab.setOnClickListener {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            val countryCode = binding.signInCountryCodeLayout.editText?.text.toString()
            val phonePostfix = binding.fragmentPhoneInput.editText?.text.toString()
            viewModel.authResultModel.observe(viewLifecycleOwner) { it.observer }
            viewModel.authPhone(requireActivity(), countryCode + phonePostfix)
        }
    }

    private val AuthResult<AuthUserModel>.observer: Unit
        get() = when (this) {
            is AuthResult.Success -> {
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                viewModel.authUser(data)
                requireActivity().restartActivity()
            }
            is AuthResult.Send -> {
                binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
                data.navigate()
            }
            is AuthResult.Failure -> {
                binding.root.showPrimarySnackBar(exception)
            }
        }

    private fun AuthUserModel.navigate() {
        val direction =
            EnterPhoneFragmentDirections.actionNavEnterPhoneToNavEnterCode(id)
        val extras =
            FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
        findNavController().navigate(direction, extras)
    }

}
