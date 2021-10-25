package st.slex.messenger.auth.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.messenger.auth.R
import st.slex.messenger.auth.databinding.FragmentEnterCodeBinding
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class EnterCodeFragment : BaseAuthFragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = checkNotNull(_binding)

    private var _id: String? = null
    private val id: String get() = checkNotNull(_id)

    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 500.toLong()
            scrimColor = Color.TRANSPARENT
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_code)
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
        val args: EnterCodeFragmentArgs by navArgs()
        _id = args.id
        editTextList[0].requestFocus()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            foreachCodeList(editTextList) { sendCode.start() }
        }
    }

    private val sendCode by lazy {
        viewLifecycleOwner.lifecycleScope.launch(
            context = Dispatchers.IO,
            start = CoroutineStart.LAZY
        ) {
            viewModel.sendCode(id = id, code = editTextList.getCodeFromList())
                .collect { collector(it) }
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
        crossinline function: () -> Unit
    ) = list.indices.forEach {
        listenCode(list[it]).also { _ ->
            if (it == list.size - 1) function()
            else list[it + 1].requestFocus()
        }
    }

    private suspend fun listenCode(editText: EditText): Unit = suspendCoroutine { continuation ->
        editText.addTextChangedListener {
            if (it?.length == 1) continuation.resumeWith(Result.success(Unit))
        }
    }

    private fun List<EditText>.getCodeFromList(): String =
        this.joinToString { it.text.toString() }.replace(", ", "")

    private suspend fun collector(result: LoginUIResult) = withContext(Dispatchers.Main) {
        when (result) {
            is LoginUIResult.Success -> {
                binding.fragmentCodeProgressIndicator.visibility = View.GONE
                //binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                val intent = Intent()
                intent.setClassName(requireContext(), "st.slex.messenger.ui.main.MainActivity")
                startActivity(intent)
                Snackbar.make(binding.root, "SUCCESS", Snackbar.LENGTH_LONG).show()
            }
            is LoginUIResult.Failure -> {
                binding.fragmentCodeProgressIndicator.visibility = View.GONE
                //binding.root.showPrimarySnackBar(result.exception.toString())
            }
            is LoginUIResult.Loading -> {
                binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



