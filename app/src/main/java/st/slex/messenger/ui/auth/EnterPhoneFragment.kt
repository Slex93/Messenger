package st.slex.messenger.ui.auth

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
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
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.authPhone(requireActivity(), countryCode + phonePostfix).collect {
                    when (it) {
                        is AuthResponse.Success -> {
                            binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                            this.async {
                                viewModel.authUser().collect { auth ->
                                    when (auth) {
                                        is VoidResponse.Success -> {
                                            requireActivity().recreate()
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
                        is AuthResponse.Send -> {
                            binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
                            val direction =
                                EnterPhoneFragmentDirections.actionNavEnterPhoneToNavEnterCode(it.id)
                            val extras =
                                FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
                            findNavController().navigate(direction, extras)
                        }
                        is AuthResponse.Failure -> {
                            binding.root.showPrimarySnackBar(it.exception.toString())
                        }
                        is AuthResponse.Loading -> {
                        }
                    }
                }
            }
        }
    }

}
