package st.slex.messenger.ui.core

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.AuthActivity
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class BaseAuthFragment : Fragment() {

    @Inject
    open lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        (requireActivity() as AuthActivity).authComponent.inject(this)
        super.onAttach(context)
    }
}