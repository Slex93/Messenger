package st.slex.messenger.auth.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.auth.databinding.FragmentEnterPhoneBinding
import java.util.*

@ExperimentalCoroutinesApi
class EnterPhoneFragment : BaseAuthFragment() {

    private var _binding: FragmentEnterPhoneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels { viewModelFactory.get() }

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
        binding.phoneEditText.addTextChangedListener(mutableListOf<InputFilter>().textListener)
        binding.fragmentPhoneFab.setOnClickListener(phoneClickListener)
    }

    private val MutableList<InputFilter>.textListener: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.phoneEditText.isTextValidInternationalPhoneNumber()) {
                    binding.fragmentPhoneFab.isEnabled = true
                    val filter = InputFilter.LengthFilter(s.toString().length)
                    add(filter)
                    binding.phoneEditText.filters = toTypedArray()
                } else {
                    binding.fragmentPhoneFab.isEnabled = false
                    clear()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        }

    private val phoneClickListener = View.OnClickListener {
        val phone = binding.phoneEditText.text.toString()
        requireActivity().lifecycleScope.launch {
            viewModel.login(phone).collect {
                it.collector()
            }
        }
    }

    private fun showKeyboard(textInputEditText: TextInputEditText) {
        textInputEditText.requestFocus()
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun LoginUIResult.collector() = when (this) {
        is LoginUIResult.Success.LogIn -> {
            requireActivity().application.onCreate()
        }
        is LoginUIResult.Success.SendCode -> {
            binding.fragmentCodeProgressIndicator.visibility = View.GONE
            //binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
            val direction =
                EnterPhoneFragmentDirections.actionNavAuthPhoneToNavAuthCode(id)
            val extras =
                FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
            findNavController().navigate(direction, extras)
        }
        is LoginUIResult.Failure -> {
            binding.fragmentCodeProgressIndicator.visibility = View.GONE
            //binding.root.showPrimarySnackBar(exception.toString())
        }
        is LoginUIResult.Loading -> {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
        }
    }

}
