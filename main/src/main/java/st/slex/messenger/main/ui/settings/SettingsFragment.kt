package st.slex.messenger.main.ui.settings

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentSettingsBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.core.UIExtensions.changeVisibility
import st.slex.messenger.main.utilites.funs.setSupportActionBar

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
        binding.settingsSignOut.setOnClickListener(signOutClickListener)
    }

    private val signOutClickListener: View.OnClickListener = View.OnClickListener {
        signOutJob.start()
    }

    private val signOutJob: Job by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO, start = CoroutineStart.LAZY
        ) {
            viewModel.signOut().collect {
                it.collector()
            }
        }
    }

    private suspend fun Resource<Nothing?>.collector(): Unit = when (this) {
        is Resource.Success -> signOutResult()
        is Resource.Failure -> signOutResult()
        is Resource.Loading -> signOutResult()
    }

    private suspend fun Resource.Success<Nothing?>.signOutResult() {
        binding.SHOWPROGRESS.changeVisibility()
        val intent = Intent().setClassName(requireContext(), AUTH_ACTIVITY)
        startActivity(intent)
        requireActivity().finish()
    }

    private suspend fun Resource.Failure<Nothing?>.signOutResult() {
        binding.SHOWPROGRESS.changeVisibility()
        Log.e(TAG, exception.message, exception.cause)
    }

    private suspend fun Resource.Loading.signOutResult() {
        binding.SHOWPROGRESS.changeVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        signOutJob.cancel(CANCEL_JOB_CAUSE)
    }

    companion object {
        private const val AUTH_ACTIVITY: String = "st.slex.messenger.auth.ui.AuthActivity"
        private const val CANCEL_JOB_CAUSE: String = "onDestroyView"
    }
}
