package com.st.slex.common.messenger.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.st.slex.common.messenger.contacts.adapter.ContactAdapter
import com.st.slex.common.messenger.contacts.model.ContactRepository
import com.st.slex.common.messenger.contacts.viewmodel.ContactViewModel
import com.st.slex.common.messenger.contacts.viewmodel.ContactViewModelFactory
import com.st.slex.common.messenger.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding
    private val repository = ContactRepository()
    private val contactViewModel: ContactViewModel by viewModels {
        ContactViewModelFactory(repository)
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel.contactList
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler = binding.fragmentContactRecycler
        adapter = ContactAdapter(clickListener)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
        contactViewModel.initContact()
        contactViewModel.contactList.observe(viewLifecycleOwner) {
            adapter.addItems(it)
        }
    }

    private val clickListener = ContactClickListener { cardView, contact ->
        val directions = ContactFragmentDirections.actionNavContactToNavSingleChat(contact)
        cardView.transitionName = "transitCard"
        val extras = FragmentNavigatorExtras(cardView to "transitCard")
        findNavController().navigate(directions, extras)
    }
}