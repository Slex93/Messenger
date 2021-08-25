package st.slex.messenger.ui.contacts

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentContactBinding
import st.slex.messenger.ui.contacts.adapter.ContactAdapter
import st.slex.messenger.ui.contacts.model.ContactRepository
import st.slex.messenger.ui.contacts.viewmodel.ContactViewModel
import st.slex.messenger.ui.contacts.viewmodel.ContactViewModelFactory

class ContactFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

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
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setHasOptionsMenu(true)
        initNavigationFields()
        setActionBar()
    }

    private fun setActionBar() {
        val toolbar = binding.fragmentContactToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.title_contacts)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home))
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

    private fun initRecyclerView() {
        recycler = binding.fragmentContactRecycler
        adapter = ContactAdapter(clickListener, this)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        contactViewModel.initContact()

        contactViewModel.contact.observe(viewLifecycleOwner) { it ->
            adapter.addItems(it)
        }
        postponeEnterTransition()
        recycler.doOnPreDraw {
            startPostponedEnterTransition()
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())


    }

    private val clickListener = ContactClickListener { cardView, contact, key ->
        val directions = ContactFragmentDirections.actionNavContactToNavSingleChat(contact, key)
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)
        findNavController().navigate(directions, extras)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_appbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        super.onStop()
        contactViewModel.contact.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}