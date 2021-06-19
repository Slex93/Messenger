package com.st.slex.common.messenger.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.databinding.FragmentEnterPhoneBinding

class EnterPhoneFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneBinding

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

        initDropMenu()
        initPhoneEditWatcher()
        initFab()
    }

    private fun initFab() {
        binding.fragmentPhoneFab.setOnClickListener {
            val countryCode = binding.signInCountryCodeLayout.editText?.text.toString()
            val phonePostfix = binding.fragmentPhoneInput.editText?.text.toString()
            val phoneNumber = countryCode + phonePostfix
            navigate(phoneNumber)
        }
    }

    private fun navigate(phoneNumber: String) {
        val direction = EnterPhoneFragmentDirections.actionNavEnterPhoneToNavEnterCode(phoneNumber)
        val extras =  FragmentNavigatorExtras(binding.fragmentPhoneFab to "activity_trans")
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