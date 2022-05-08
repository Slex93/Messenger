package st.slex.messenger.main.ui.settings

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentSettingsBinding
import st.slex.messenger.main.ui.MainActivity
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.ui.core.UIExtensions.changeVisibility
import st.slex.messenger.main.utilites.funs.setSupportActionBar
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = checkNotNull(_binding)

    private lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory.get() }

    @Inject
    fun injection(viewModelFactory: Lazy<ViewModelProvider.Factory>) {
        this.viewModelFactory = viewModelFactory
    }

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

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
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.signOut().collect {
                it.collector()
            }
        }
    }

    private suspend fun Resource<Nothing?>.collector() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            when (this@collector) {
                is Resource.Success -> signOutResult()
                is Resource.Failure -> signOutResult()
                is Resource.Loading -> loading()
            }
        }
    }

    private suspend fun signOutResult() {
        binding.SHOWPROGRESS.changeVisibility()
        val intent = Intent().setClassName(requireContext(), SPLASH_ACTIVITY)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    private suspend fun Resource.Failure<Nothing?>.signOutResult() {
        binding.SHOWPROGRESS.changeVisibility()
        Log.e(TAG, exception.message, exception.cause)
    }

    private suspend fun loading() {
        binding.SHOWPROGRESS.changeVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SPLASH_ACTIVITY: String = "st.slex.messenger.splashscreen.SplashActivity"
    }
}
