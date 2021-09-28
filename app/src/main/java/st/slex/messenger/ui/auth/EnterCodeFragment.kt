package st.slex.messenger.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.messenger.ui.core.BaseAuthFragment
import st.slex.messenger.ui.core.VoidUIResult
import st.slex.messenger.ui.main.MainActivity
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.funs.start

@ExperimentalCoroutinesApi
class EnterCodeFragment : BaseAuthFragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
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
        val id = args.id
        val editTextList = getEditTextList()
        editTextList[0].requestFocus()
        editTextList.foreachCodeList {
            launchAuth(id, editTextList.getCodeFromList())
        }
    }

    private fun getEditTextList() = listOf(
        binding.codeEditText1,
        binding.codeEditText2,
        binding.codeEditText3,
        binding.codeEditText4,
        binding.codeEditText5,
        binding.codeEditText6
    )

    private inline fun List<EditText>.foreachCodeList(crossinline function: () -> Unit) {
        for (number in this.indices) {
            lifecycleScope.launch {
                listenCode(this@foreachCodeList[number]).collect {
                    if (number == this@foreachCodeList.size - 1) {
                        function()
                    } else {
                        this@foreachCodeList[number + 1].requestFocus()
                    }
                    this.cancel()
                }
            }
        }
    }

    private suspend fun listenCode(editText: EditText): Flow<Nothing?> = callbackFlow {
        editText.addTextChangedListener {
            if (it?.length == 1) trySendBlocking(null)
        }
        awaitClose { }
    }

    private fun launchAuth(id: String, code: String) = requireActivity().lifecycleScope.launch {
        Log.i("testCOde", code)
        viewModel.sendCode(id = id, code = code).collect {
            it.collector()
        }
    }

    private fun List<EditText>.getCodeFromList(): String =
        this.joinToString { it.text.toString() }.replace(", ", "")

    private fun VoidUIResult.collector() {
        when (this) {
            is VoidUIResult.Success -> {
                binding.fragmentCodeProgressIndicator.visibility = View.GONE
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                requireActivity().start(MainActivity())
            }
            is VoidUIResult.Failure -> {
                binding.fragmentCodeProgressIndicator.visibility = View.GONE
                binding.root.showPrimarySnackBar(exception.toString())
            }
            is VoidUIResult.Loading -> {
                binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



