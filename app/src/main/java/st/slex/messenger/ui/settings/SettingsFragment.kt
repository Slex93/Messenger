package st.slex.messenger.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSettingsBinding
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.restartActivity
import st.slex.messenger.utilites.funs.setSupportActionBar
import st.slex.messenger.utilites.result.VoidResponse

@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsToolbar.title = getString(R.string.title_contacts)
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsSignOut.setOnClickListener {

            viewModel.signOut().observe(viewLifecycleOwner) {
                when (it) {
                    is VoidResponse.Success -> {
                        requireActivity().restartActivity()
                    }
                    is VoidResponse.Failure -> {
                        Log.i("$this", it.exception.toString())
                    }
                    is VoidResponse.Loading -> {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}