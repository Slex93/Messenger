package st.slex.messenger.auth.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import st.slex.messenger.auth.R
import st.slex.messenger.auth.databinding.FragmentEnterCodeBinding
import st.slex.messenger.auth.ui.core.LoginUIResult
import javax.inject.Inject


@ExperimentalCoroutinesApi
class EnterCodeFragment : Fragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: EnterCodeFragmentArgs by navArgs()

    private lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    private var sendCodeJob: Job = Job()
    private var collectorJob: Job = Job()

    @Inject
    fun injection(viewModelFactory: Lazy<ViewModelProvider.Factory>) {
        this.viewModelFactory = viewModelFactory
    }

    override fun onAttach(context: Context) {
        requireActivity().authComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 500.toLong()
            scrimColor = Color.TRANSPARENT
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_code)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.requestFocus()
        binding.editText.addTextChangedListener {
            if (it?.length == 6) sendCode()
        }
    }

    private fun sendCode() {
        sendCodeJob.cancel()
        sendCodeJob = viewLifecycleOwner.lifecycleScope.launch(context = Dispatchers.IO) {
            viewModel.sendCode(
                id = args.id,
                code = binding.editText.text.toString()
            ).collect(::collector)
        }
    }

    private fun collector(resource: LoginUIResult) {
        collectorJob.cancel()
        collectorJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            when (resource) {
                is LoginUIResult.Success -> success()
                is LoginUIResult.Failure -> failure(resource.exception)
                is LoginUIResult.Loading -> showProgress()
            }
        }
    }

    private fun failure(exception: Exception) {
        binding.editText.setText("")
        stopProgress()
        showError(exception)
    }

    private fun success() {
        stopProgress()
        val intent = Intent()
        intent.setClassName(requireContext(), MAIN_ACTIVITY_PATH)
        Snackbar.make(binding.root, "SUCCESS", Snackbar.LENGTH_LONG).show()
        startActivity(intent)
        requireActivity().finish()
    }

    private fun stopProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.GONE
    }

    private fun showProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sendCodeJob.cancel()
        collectorJob.cancel()
        _binding = null
    }

    private fun showError(throwable: Throwable) {
        if (isVisible) {
            Snackbar.make(
                binding.root,
                throwable.message.toString(),
                Snackbar.LENGTH_SHORT
            ).apply {
                setAction("OK") {}
                (view.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.TOP
            }.show()
        }
        Log.e(TAG, throwable.stackTraceToString())
    }

    companion object {
        private const val MAIN_ACTIVITY_PATH = "st.slex.messenger.main.ui.MainActivity"
        private const val TAG = "EnterCodeFragment"
    }
}



