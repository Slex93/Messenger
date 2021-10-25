package st.slex.messenger.main.ui.core

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.ui.main.MainActivity
import st.slex.messenger.main.utilites.base.GlideBase
import st.slex.messenger.main.utilites.base.SetImageWithGlide
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class BaseFragment : Fragment() {

    @Inject
    open lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        (requireActivity() as MainActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

    open val glide = SetImageWithGlide { imageView, url, needCrop, needCircleCrop, needOriginal ->
        GlideBase {
            startPostponedEnterTransition()
        }.setImageWithRequest(imageView, url, needCrop, needCircleCrop, needOriginal)
    }
}