package st.slex.messenger.utilites.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import st.slex.messenger.utilites.appComponent
import javax.inject.Inject

open class BaseFragment : Fragment() {
    @Inject
    open lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    @Inject
    open lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().applicationContext.appComponent.inject(this)
    }
}