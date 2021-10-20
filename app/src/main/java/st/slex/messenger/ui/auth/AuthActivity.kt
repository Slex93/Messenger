package st.slex.messenger.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.common.messenger.databinding.ActivityAuthBinding
import st.slex.messenger.appComponent
import st.slex.messenger.di.auth.AuthComponent
import st.slex.messenger.di.auth.DaggerAuthComponent

@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding: ActivityAuthBinding
        get() = checkNotNull(_binding)

    private var _authComponent: AuthComponent? = null
    val authComponent: AuthComponent
        get() = checkNotNull(_authComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        _authComponent = DaggerAuthComponent.builder()
            .activity(this)
            .appComponent(appComponent)
            .create()
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