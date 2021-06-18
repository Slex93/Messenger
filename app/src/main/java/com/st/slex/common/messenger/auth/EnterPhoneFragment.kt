package com.st.slex.common.messenger.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}