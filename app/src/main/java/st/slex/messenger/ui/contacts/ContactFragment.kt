package st.slex.messenger.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentContactBinding
import st.slex.messenger.ui.contacts.adapter.ContactAdapter
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.utilites.funs.setSupportActionBar

@ExperimentalCoroutinesApi
class ContactFragment : BaseFragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private val contactViewModel: ContactViewModel by viewModels {
        viewModelFactory.get()
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.fragmentContactToolbar.title = getString(R.string.title_contacts)
        setSupportActionBar(binding.fragmentContactToolbar)
        initRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            contactViewModel.getContacts().collect {
                it.collect()
            }
        }
    }

    private fun UIResult<List<ContactsUI>>.collect() {
        when (this) {
            is UIResult.Success -> {
                adapter.addItems(data)
            }
            is UIResult.Failure -> {
                Log.e(
                    "Exception in ContactList from the flow",
                    exception.message.toString(),
                    exception.cause
                )
            }
            is UIResult.Loading -> {

            }
        }
    }

    private fun initRecyclerView() {
        recycler = binding.fragmentContactRecycler
        adapter = ContactAdapter(OpenChat())
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        postponeEnterTransition()
        recycler.doOnPreDraw {
            startPostponedEnterTransition()
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private inner class OpenChat : ClickListener<ContactsUI> {
        override fun click(item: ContactsUI) {
            item.startChat { card, url ->
                val directions =
                    ContactFragmentDirections.actionNavContactToNavSingleChat(
                        card.transitionName,
                        url
                    )
                val extras = FragmentNavigatorExtras(card to card.transitionName)
                findNavController().navigate(directions, extras)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_appbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}