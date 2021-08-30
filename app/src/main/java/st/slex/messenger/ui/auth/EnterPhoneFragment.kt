package st.slex.messenger.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.restartActivity
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.result.AuthResult

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            viewModel.authPhone(requireActivity(), countryCode + phonePostfix)
                .observe(viewLifecycleOwner, observer)
        }
    }

    private val observer: Observer<AuthResult>
        get() = Observer {
            when (it) {
                is AuthResult.Success -> {
                    binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                    viewModel.authUser()
                    requireActivity().restartActivity()
                }
                is AuthResult.Send -> {
                    binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
                    it.id.navigate()
                }
                is AuthResult.Failure -> {
                    binding.root.showPrimarySnackBar(it.exception.toString())
                }
            }
        }

    private fun String.navigate() {
        val direction =
            EnterPhoneFragmentDirections.actionNavEnterPhoneToNavEnterCode(this)
        val extras =
            FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
        findNavController().navigate(direction, extras)
    }

}
