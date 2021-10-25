package st.slex.messenger

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.ui.AuthActivity
import st.slex.messenger.main.ui.main.MainActivity

@Module
class AppModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun providesInitialActivity(application: Application): Intent =
        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(application, MainActivity::class.java)
        } else {
            Intent(application, AuthActivity::class.java)
        }
}