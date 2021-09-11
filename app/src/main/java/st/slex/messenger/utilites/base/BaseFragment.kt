package st.slex.messenger.utilites.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.utilites.funs.appComponent
import javax.inject.Inject

@ExperimentalCoroutinesApi
open class BaseFragment : Fragment() {
    @Inject
    open lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    open val glide = SetImageWithGlide { imageView, url, needCrop, needCircleCrop ->
        GlideBase {
            startPostponedEnterTransition()
        }.setImageWithRequest(imageView, url, needCrop, needCircleCrop)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().applicationContext.appComponent.inject(this)
    }
}