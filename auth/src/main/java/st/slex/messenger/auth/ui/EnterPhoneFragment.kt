package st.slex.messenger.auth.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import st.slex.messenger.auth.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.auth.ui.core.LoginUIResult
import st.slex.resources.KeyboardUtil
import st.slex.resources.KeyboardUtilImpl
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EnterPhoneFragment : Fragment() {

    private var _binding: FragmentEnterPhoneBinding? = null
    private val binding get() = checkNotNull(_binding)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AuthViewModel by viewModels { viewModelFactory }
    private var phoneClickJob: Job = Job()
    private var collectorJob: Job = Job()

    private var _weakReference: WeakReference<Activity>? = null
    private val weakReference: WeakReference<Activity>
        get() = checkNotNull(_weakReference)

    @Inject
    fun injection(viewModelFactory: ViewModelProvider.Factory) {
        this.viewModelFactory = viewModelFactory
    }

    override fun onAttach(context: Context) {
        requireActivity().authComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = FRAGMENT_TITLE
        _weakReference = WeakReference(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keyboardUtil: KeyboardUtil = KeyboardUtilImpl(weakReference)
        binding.phoneEditText.requestFocus()
        binding.phoneEditText.setRegionCode(Locale.getDefault().country)
        binding.phoneEditText.addTextChangedListener {
            if (binding.phoneEditText.isTextValidInternationalPhoneNumber()) {
                binding.fragmentPhoneFab.isEnabled = true
                keyboardUtil.hideKeyboard()
            } else {
                binding.fragmentPhoneFab.isEnabled = false
            }
        }
        binding.fragmentPhoneFab.setOnClickListener(phoneClickListener)
    }

    private val phoneClickListener = View.OnClickListener {
        phoneClickJob.cancel()
        phoneClickJob = requireActivity().lifecycleScope.launch(Dispatchers.IO) {
            val phone = binding.phoneEditText.text.toString()
            viewModel.login(phone).collect(::collector)
        }
    }

    private fun collector(resource: LoginUIResult) {
        collectorJob.cancel()
        collectorJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            when (resource) {
                is LoginUIResult.Success.LogIn -> resultLogIn()
                is LoginUIResult.Success.SendCode -> resultSendCode(resource.id)
                is LoginUIResult.Failure -> failureResult(resource.exception)
                is LoginUIResult.Loading -> showProgress()
            }
        }
    }

    private fun failureResult(exception: Exception) {
        stopProgress()
        exception.cause?.let(::showError)
    }

    private fun resultLogIn() {
        val intent = Intent()
        intent.setClassName(requireContext(), MAIN_ACTIVITY_PATH)
        startActivity(intent)
    }

    private fun resultSendCode(id: String) {
        stopProgress()
        val direction = EnterPhoneFragmentDirections.actionNavCode(id)
        val sharedElements = binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName
        val extras = FragmentNavigatorExtras(sharedElements)
        findNavController().navigate(direction, extras)
    }

    private fun stopProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.GONE
    }

    private fun showProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        weakReference.clear()
        _weakReference = null
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneClickJob.cancel()
        collectorJob.cancel()
    }

    private fun showError(throwable: Throwable) {
        if (isVisible) {
            Snackbar.make(
                requireView(),
                throwable.message.toString(),
                Snackbar.LENGTH_SHORT
            ).apply {
                setBackgroundTint(Color.CYAN)
                setAction("OK") {}
            }.show()
        }
        Log.e(TAG, throwable.stackTraceToString())
    }

    companion object {
        private const val MAIN_ACTIVITY_PATH = "st.slex.messenger.main.ui.MainActivity"
        private const val FRAGMENT_TITLE = "Phone number"
        private const val TAG = "EnterPhoneFragment"
    }
}
