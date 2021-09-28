package st.slex.messenger.ui.auth

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.ui.core.BaseAuthFragment
import st.slex.messenger.ui.main.MainActivity
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.funs.start
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

        val country = Locale.getDefault().country
        binding.phoneEditText.setRegionCode(country)

        val filters = mutableListOf<InputFilter>()

        binding.phoneEditText.addTextChangedListener {
            if (binding.phoneEditText.isTextValidInternationalPhoneNumber()) {
                binding.fragmentPhoneFab.isEnabled = true
                val filter = InputFilter.LengthFilter(it.toString().length)
                filters.add(filter)
                binding.phoneEditText.filters = filters.toTypedArray()
            } else {
                binding.fragmentPhoneFab.isEnabled = false
                filters.clear()
            }
        }

        binding.fragmentPhoneFab.setOnClickListener {
            val phone = binding.phoneEditText.text.toString()
            requireActivity().lifecycleScope.launch {
                viewModel.login(phone).collect {
                    it.collector()
                }
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
        is LoginUIResult.Success -> {
            requireActivity().start(MainActivity())
        }
        is LoginUIResult.SendCode -> {
            binding.fragmentCodeProgressIndicator.visibility = View.GONE
            binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
            val direction =
                EnterPhoneFragmentDirections.actionNavAuthPhoneToNavAuthCode(id)
            val extras =
                FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
            findNavController().navigate(direction, extras)
        }
        is LoginUIResult.Failure -> {
            binding.fragmentCodeProgressIndicator.visibility = View.GONE
            binding.root.showPrimarySnackBar(exception.toString())
        }
        is LoginUIResult.Loading -> {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
        }
    }

}
