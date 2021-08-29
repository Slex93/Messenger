package st.slex.messenger.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSettingsBinding
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.setSupportActionBar

class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

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
            auth.signOut()
            requireActivity().restartActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}