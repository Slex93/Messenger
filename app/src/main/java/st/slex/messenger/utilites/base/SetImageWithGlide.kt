package st.slex.messenger.utilites.base

import android.widget.ImageView

class SetImageWithGlide(
    val makeGlideImage: (
        imageView: ImageView,
        url: String,
        needCrop: Boolean,
        needCircleCrop: Boolean
    ) -> Unit
) {

    fun setImage(
        imageView: ImageView,
        url: String,
        needCrop: Boolean = false,
        needCircleCrop: Boolean = false
    ) = makeGlideImage(imageView, url, needCrop, needCircleCrop)

}