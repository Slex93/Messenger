package st.slex.messenger.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterPhoneBinding
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.funs.start
import st.slex.messenger.utilites.result.AuthResponse

@ExperimentalCoroutinesApi
class EnterPhoneFragment : BaseFragment() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.fragmentPhoneInput.editText?.addTextChangedListener {
            binding.fragmentPhoneFab.isEnabled = it?.length == 12
        }
        binding.fragmentPhoneFab.setOnClickListener {
            binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            val phone = binding.fragmentPhoneInput.editText?.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.login(phone, requireActivity()).collect {
                    it.collector()
                }
            }
        }
    }

    private fun AuthResponse.collector() = when (this) {
        is AuthResponse.Success -> {
            binding.root.showPrimarySnackBar(getString(R.string.snack_success))
            requireActivity().start(MainActivity())
        }
        is AuthResponse.Send -> {
            Log.i("checkId::PhoneFragment", this.id)
            binding.root.showPrimarySnackBar(getString(R.string.snack_code_send))
            val direction =
                EnterPhoneFragmentDirections.actionNavAuthPhoneToNavAuthCode(this.id)
            val extras =
                FragmentNavigatorExtras(binding.fragmentPhoneFab to binding.fragmentPhoneFab.transitionName)
            findNavController().navigate(direction, extras)
        }
        is AuthResponse.Failure -> {
            binding.root.showPrimarySnackBar(exception.toString())
        }
        is AuthResponse.Loading -> {
        }
    }

}
