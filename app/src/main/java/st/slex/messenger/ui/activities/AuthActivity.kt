package st.slex.messenger.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.databinding.ActivityAuthBinding
import st.slex.messenger.utilites.funs.appComponent
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthActivity @Inject constructor() : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        appComponent.inject(this)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}