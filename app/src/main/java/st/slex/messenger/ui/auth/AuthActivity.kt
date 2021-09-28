package st.slex.messenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.databinding.ActivityAuthBinding
import st.slex.messenger.di.component.AuthComponent
import st.slex.messenger.di.component.DaggerAuthComponent
import st.slex.messenger.di.module.auth.AuthModule

@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    private var _authComponent: AuthComponent? = null
    val authComponent: AuthComponent get() = _authComponent!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _authComponent = DaggerAuthComponent
            .builder()
            .authModule(AuthModule(this))
            .build()
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _authComponent = null
    }
}