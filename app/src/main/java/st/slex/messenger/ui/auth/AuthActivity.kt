package st.slex.messenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.databinding.ActivityAuthBinding
import st.slex.messenger.di.component.AuthComponent
import st.slex.messenger.utilites.funs.appComponent

@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!
    lateinit var authComponent: AuthComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent = appComponent.authComponent().create()
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}