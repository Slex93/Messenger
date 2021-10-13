package st.slex.messenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.databinding.ActivityAuthBinding
import st.slex.messenger.appComponent
import st.slex.messenger.di.component.AuthSubcomponent

@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    private var _authComponent: AuthSubcomponent? = null
    val authComponent: AuthSubcomponent get() = _authComponent!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _authComponent = appComponent.authBuilder.activity(this).create()
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