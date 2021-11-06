package st.slex.messenger.main.ui.core

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialContainerTransform
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.R
import st.slex.messenger.main.ui.MainActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    open val glide = SetImageWithGlide { imageView, url, needCrop, needCircleCrop, needOriginal ->
        GlideBase {
            startPostponedEnterTransition()
        }.setImageWithRequest(imageView, url, needCrop, needCircleCrop, needOriginal)
    }
}