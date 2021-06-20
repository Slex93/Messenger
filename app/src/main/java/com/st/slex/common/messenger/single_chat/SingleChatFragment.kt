package com.st.slex.common.messenger.single_chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.contacts.model.Contact
import com.st.slex.common.messenger.databinding.FragmentSingleChatBinding


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setAnimation()
        setTransitAnimation()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeExtras()
        initNavigationFields()
        setActionBar()
    }

    private fun setAnimation() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 1000
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 1000
        }
        enterTransition = MaterialElevationScale(true).apply {
            duration = 1000
        }
    }

    private fun takeExtras() {
        val args:SingleChatFragmentArgs by navArgs()
        val contact: Contact = args.contact
        val key = args.key
        Log.i("Transit::Chat", key)
        binding.toolbarInfo.toolbarInfoCardView.transitionName = key
        binding.toolbarInfo.toolbarInfoUsername.text = contact.fullname
        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", key)
    }

    private fun setTransitAnimation() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    private fun setActionBar() {
        val toolbar = binding.chatToolbar
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_contact))
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

}