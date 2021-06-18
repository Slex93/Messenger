package com.st.slex.common.messenger.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.st.slex.common.messenger.databinding.FragmentMainBinding
import com.st.slex.common.messenger.main_screen.adapter.MainAdapter
import com.st.slex.common.messenger.main_screen.model.MainScreenDatabase
import com.st.slex.common.messenger.main_screen.model.MainScreenRepository
import com.st.slex.common.messenger.main_screen.model.base.MainMessage
import com.st.slex.common.messenger.main_screen.viewmodel.MainScreenViewModel
import com.st.slex.common.messenger.main_screen.viewmodel.MainScreenViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val database = MainScreenDatabase()
    private val repository by lazy { MainScreenRepository(database) }

    private val mainScreenViewModel: MainScreenViewModel by viewModels {
        MainScreenViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter()
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        mainScreenViewModel.mainMessage.observe(viewLifecycleOwner) {
            adapter.makeMainList(it as MutableList<MainMessage>)
        }
    }

}