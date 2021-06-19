package com.st.slex.common.messenger.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialContainerTransform
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.databinding.FragmentEnterCodeBinding

class EnterCodeFragment : Fragment() {

    private lateinit var binding: FragmentEnterCodeBinding

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
/*
            setAllContainerColors(requireContext().setTheme(R.attr.colorSurface))
*/

        }
    }
}