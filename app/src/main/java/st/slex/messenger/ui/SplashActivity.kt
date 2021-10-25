package st.slex.messenger.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.ui.AuthActivity
import st.slex.messenger.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
@ExperimentalCoroutinesApi
class SplashActivity : AppCompatActivity() {

    private val initialJob = lifecycleScope.launchWhenCreated {
        val intent = if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, AuthActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        initialJob.cancel()
    }
}