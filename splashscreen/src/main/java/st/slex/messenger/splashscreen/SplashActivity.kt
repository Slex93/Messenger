package st.slex.messenger.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val initialJob = lifecycleScope.launchWhenCreated {
        val intent = if (FirebaseAuth.getInstance().currentUser != null) {
            Intent().setClassName(
                this@SplashActivity,
                "st.slex.messenger.main.ui.MainActivity"
            )
        } else {
            Intent().setClassName(
                this@SplashActivity,
                "st.slex.messenger.auth.ui.AuthActivity"
            )
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        initialJob.cancel()
    }
}