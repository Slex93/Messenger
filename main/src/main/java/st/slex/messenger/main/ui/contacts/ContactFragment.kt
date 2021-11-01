package st.slex.messenger.main.ui.contacts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentContactBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.core.ClickListener
import st.slex.messenger.main.utilites.funs.setSupportActionBar


@ExperimentalCoroutinesApi
class ContactFragment : BaseFragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: ContactViewModel by viewModels { viewModelFactory.get() }

    private val adapter: ContactAdapter by lazy {
        ContactAdapter(ItemClick(), viewModel::getUser, viewLifecycleOwner.lifecycleScope)
    }

    private val jobListeningContacts: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO, start = CoroutineStart.LAZY
        ) {
            viewModel.getAllContacts().collect {
                if (it is Resource.Success) launch(Dispatchers.Main) {
                    adapter.setItems(it.data)
                }
            }
        }
    }

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
        binding.recyclerView.adapter = adapter
        jobListeningContacts.start()
    }

    private inner class ItemClick : ClickListener<ContactUI> {
        override fun click(item: ContactUI) {
            item.openChat { card, url ->
                val transitionName = card.transitionName
                val directions = ContactFragmentDirections.navContactToChat(transitionName, url)
                val extras = FragmentNavigatorExtras(card to transitionName)
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
        jobListeningContacts.cancel()
    }
}