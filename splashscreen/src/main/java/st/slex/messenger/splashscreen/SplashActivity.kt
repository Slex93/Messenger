package st.slex.messenger.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    init {
        lifecycleScope.launchWhenCreated {
            val user = FirebaseAuth.getInstance().currentUser
            val className = if (user == null) AUTH_ACTIVITY else MAIN_ACTIVITY
            val intent = Intent().setClassName(this@SplashActivity, className)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val MAIN_ACTIVITY = "st.slex.messenger.main.ui.MainActivity"
        private const val AUTH_ACTIVITY = "st.slex.messenger.auth.ui.AuthActivity"
    }
}