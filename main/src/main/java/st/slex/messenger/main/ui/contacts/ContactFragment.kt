package st.slex.messenger.main.ui.contacts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.core.FirebaseConstants.NODE_CONTACT
import st.slex.messenger.core.FirebaseConstants.NODE_USER
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

    private val query: Query by lazy {
        FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(Firebase.auth.uid.toString())
            .child(NODE_CONTACT)
    }

    private val parser: SnapshotParser<ContactUI> = SnapshotParser {
        return@SnapshotParser it.getValue(ContactUI.Base::class.java)!!
    }

    private val options: FirebaseRecyclerOptions<ContactUI> by lazy {
        FirebaseRecyclerOptions.Builder<ContactUI>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(query, parser)
            .build()
    }

    private val adapter: ContactAdapter by lazy {
        ContactAdapter(options, ItemClick(), viewModel::getUser, viewLifecycleOwner.lifecycleScope)
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
    }
}