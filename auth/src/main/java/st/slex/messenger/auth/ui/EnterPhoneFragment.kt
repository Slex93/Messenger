package st.slex.messenger.auth.ui

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import st.slex.messenger.auth.databinding.FragmentEnterPhoneBinding
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EnterPhoneFragment : Fragment() {

    private var _binding: FragmentEnterPhoneBinding? = null
    private val binding get() = checkNotNull(_binding)

    private lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
    private val viewModel: AuthViewModel by viewModels { viewModelFactory.get() }
    private var phoneClickJob: Job = Job()

    @Inject
    fun injection(viewModelFactory: Lazy<ViewModelProvider.Factory>) {
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
        (activity as AppCompatActivity).supportActionBar?.title = "Phone Number"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard(binding.phoneEditText)
        binding.phoneEditText.setRegionCode(Locale.getDefault().country)
        binding.phoneEditText.addTextChangedListener {
            binding.fragmentPhoneFab.isEnabled =
                binding.phoneEditText.isTextValidInternationalPhoneNumber()
        }
        binding.fragmentPhoneFab.setOnClickListener(phoneClickListener)
    }

    private val phoneClickListener = View.OnClickListener {
        phoneClickJob.cancel()
        val phone = binding.phoneEditText.text.toString()
        phoneClickJob = requireActivity().lifecycleScope.launch(Dispatchers.IO) {
            viewModel.login(phone).collect(::collector)
        }
    }

    private fun showKeyboard(textInputEditText: TextInputEditText) {
        textInputEditText.requestFocus()
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun collector(resource: LoginUIResult) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            when (resource) {
                is LoginUIResult.Success.LogIn -> resultLogIn()
                is LoginUIResult.Success.SendCode -> resource.resultSendCode()
                is LoginUIResult.Failure -> stopProgress()
                is LoginUIResult.Loading -> showProgress()
            }
        }
    }

    private fun resultLogIn() {
        val intent = Intent()
        intent.setClassName(requireContext(), MAIN_ACTIVITY_PATH)
        startActivity(intent)
    }

    private fun LoginUIResult.Success.SendCode.resultSendCode() {
        binding.fragmentCodeProgressIndicator.visibility = View.GONE
        val direction = EnterPhoneFragmentDirections.actionNavAuthPhoneToNavAuthCode(id)
        val extras =
            FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneClickJob.cancel()
    }

    companion object {
        private const val MAIN_ACTIVITY_PATH = "st.slex.messenger.main.ui.MainActivity"
    }
}
