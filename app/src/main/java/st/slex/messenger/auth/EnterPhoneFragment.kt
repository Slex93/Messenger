package st.slex.messenger.auth

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
import st.slex.messenger.auth.model.AuthRepository
import st.slex.messenger.auth.viewmodel.AuthViewModel
import st.slex.messenger.auth.viewmodel.AuthViewModelFactory
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.showPrimarySnackBar

class EnterPhoneFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneBinding

    private val repository = AuthRepository()
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Phone Number"
        initDropMenu()
        initPhoneEditWatcher()
        initFab()
    }

    private fun initFab() {
        binding.fragmentPhoneFab.setOnClickListener {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            val countryCode = binding.signInCountryCodeLayout.editText?.text.toString()
            val phonePostfix = binding.fragmentPhoneInput.editText?.text.toString()
            val phoneNumber = countryCode + phonePostfix
            sendCode(phoneNumber)
        }
    }

    private fun sendCode(phoneNumber: String) {
        authViewModel.callbackReturnStatus.observe(viewLifecycleOwner) {
            when (it) {
                "success" -> {
                    binding.root.showPrimarySnackBar(it)
                    requireActivity().restartActivity()
                }
                "send" -> {
                    binding.root.showPrimarySnackBar(it)
                    navigate(phoneNumber)
                }
                else -> {
                    binding.root.showPrimarySnackBar(it)
                }
            }
        }
        authViewModel.initPhoneNumber(phoneNumber, requireActivity())
    }

    private fun navigate(phoneNumber: String) {
        val direction = EnterPhoneFragmentDirections.actionNavEnterPhoneToNavEnterCode(phoneNumber)
        val extras = FragmentNavigatorExtras(binding.fragmentPhoneFab to "activity_trans")
        findNavController().navigate(direction, extras)
    }

    private fun initPhoneEditWatcher() {
        binding.fragmentPhoneInput.editText?.addTextChangedListener {
            binding.fragmentPhoneFab.isEnabled = it?.length == 10
        }
    }

    private fun initDropMenu() {
        val items = listOf("+7", "+8", "+9", "+10")
        val adapter = ArrayAdapter(requireContext(), R.layout.sign_in_list_item, items)
        (binding.signInCountryCodeLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }

}
