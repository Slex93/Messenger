package com.st.slex.common.messenger.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onStart() {
        super.onStart()
        binding.fragmentPhoneFab.setOnClickListener {
            findNavController().navigate(R.id.action_nav_enter_phone_to_nav_enter_code)
        }
    }

}