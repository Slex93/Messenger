package st.slex.messenger.ui.contacts

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingConfig
import com.firebase.ui.database.SnapshotParser
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentContactBinding
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.funs.setSupportActionBar


@ExperimentalCoroutinesApi
class ContactFragment : BaseFragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val adapter: ContactAdapter by lazy {
        val query: Query = FirebaseDatabase
            .getInstance().reference
            .child(NODE_USER)
            .child(Firebase.auth.uid.toString())
            .child(NODE_CONTACT)
        val config = PagingConfig(10, 10, false)
        val options = DatabasePagingOptions.Builder<ContactUIModel>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(query, config, SnapshotParser {
                return@SnapshotParser it.getValue(ContactUIModel.Base::class.java)!!
            })
            .build()
        ContactAdapter(options, ItemClick())
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

    private inner class ItemClick : ClickListener<ContactUIModel> {
        override fun click(item: ContactUIModel) {
            item.openChat { card, url ->
                val directions = ContactFragmentDirections.actionNavContactToNavSingleChat(
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