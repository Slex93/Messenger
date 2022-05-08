package st.slex.messenger.auth.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.auth.R
import st.slex.messenger.auth.databinding.FragmentEnterCodeBinding
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class EnterCodeFragment : Fragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: EnterCodeFragmentArgs by navArgs()

    private lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    @Inject
    fun injection(viewModelFactory: Lazy<ViewModelProvider.Factory>) {
        this.viewModelFactory = viewModelFactory
    }

    override fun onAttach(context: Context) {
        (requireActivity() as AuthActivity).authComponent.inject(this)
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
        editTextList.first().requestFocus()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            foreachCodeList(editTextList) { sendCode.start() }
        }
    }

    private val sendCode by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO,
            start = CoroutineStart.LAZY
        ) {
            viewModel.sendCode(id = args.id, code = codeFromList).collect(::collector)
        }
    }

    private val editTextList: List<EditText> by lazy {
        listOf(
            binding.codeEditText1, binding.codeEditText2, binding.codeEditText3,
            binding.codeEditText4, binding.codeEditText5, binding.codeEditText6
        )
    }

    private suspend inline fun foreachCodeList(
        list: List<EditText>,
        crossinline startSendingCode: () -> Unit
    ) = list.indices.forEach {
        listenCode(list[it]).also { _ ->
            if (it == list.size - 1) startSendingCode()
            else list[it + 1].requestFocus()
        }
    }

    private suspend fun listenCode(editText: EditText): Unit = suspendCoroutine { continuation ->
        editText.addTextChangedListener {
            if (it?.length == 1) continuation.resumeWith(Result.success(Unit))
        }
    }

    private val codeFromList: String
        get() = editTextList.joinToString { it.text.toString() }.replace(", ", "")

    private fun collector(
        resource: LoginUIResult
    ) = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        when (resource) {
            is LoginUIResult.Success -> success()
            is LoginUIResult.Failure -> stopProgress()
            is LoginUIResult.Loading -> showProgress()
        }
    }

    private fun success() {
        stopProgress()
        val intent = Intent()
        intent.setClassName(requireContext(), MAIN_ACTIVITY_PATH)
        startActivity(intent)
        requireActivity().finish()
        Snackbar.make(binding.root, "SUCCESS", Snackbar.LENGTH_LONG).show()
    }

    private fun showProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.fragmentCodeProgressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAIN_ACTIVITY_PATH = "st.slex.messenger.main.ui.MainActivity"
    }
}



