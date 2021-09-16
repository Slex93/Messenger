package st.slex.messenger.ui.core

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.utilites.base.GlideBase
import st.slex.messenger.utilites.base.SetImageWithGlide
import st.slex.messenger.utilites.funs.appComponent
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class BaseFragment : Fragment() {

    @Inject
    open lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    open val glide = SetImageWithGlide { imageView, url, needCrop, needCircleCrop, needOriginal ->
        GlideBase {
            startPostponedEnterTransition()
        }.setImageWithRequest(imageView, url, needCrop, needCircleCrop, needOriginal)
    }
}