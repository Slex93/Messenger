package st.slex.messenger.main.ui.core

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.R
import st.slex.messenger.main.utilites.base.GlideBase
import st.slex.messenger.main.utilites.base.SetImageWithGlide

@ExperimentalCoroutinesApi
open class BaseFragment : Fragment() {

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