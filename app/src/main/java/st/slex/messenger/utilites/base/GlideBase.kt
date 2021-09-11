package st.slex.messenger.utilites.base

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import st.slex.common.messenger.R

@JvmInline
value class GlideBase(
    val startPostponedEnterTransition: () -> Unit
) {
    @SuppressLint("CheckResult")
    fun setImageWithRequest(
        imageView: ImageView,
        url: String,
        needCrop: Boolean = false,
        needCircleCrop: Boolean = false
    ) {
        val glide = Glide.with(imageView)
            .load(url)
            .listener(primaryRequestListener)
            .apply(RequestOptions.placeholderOf(R.drawable.test_image))
        if (needCrop) {
            glide.centerCrop()
        }
        if (needCircleCrop) glide.circleCrop()
        glide.into(imageView)
    }

    private val primaryRequestListener: RequestListener<Drawable>
        get() = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }
        }
}