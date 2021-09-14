package st.slex.messenger.utilites.base

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
open class BaseFragment : RawFragment() {
    open val glide = SetImageWithGlide { imageView, url, needCrop, needCircleCrop, needOriginal ->
        GlideBase {
            startPostponedEnterTransition()
        }.setImageWithRequest(imageView, url, needCrop, needCircleCrop, needOriginal)
    }
}