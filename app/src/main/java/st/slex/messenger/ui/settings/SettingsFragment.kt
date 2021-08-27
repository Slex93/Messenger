package st.slex.messenger.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSettingsBinding
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.restartActivity
import st.slex.messenger.utilites.setSupportActionBar

class SettingsFragment : Fragment() {

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
            AUTH.signOut()
            requireActivity().restartActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}