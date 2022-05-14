package st.slex.messenger.main.utilites.base

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import st.slex.messenger.main.R

//TODO Refactor this
@JvmInline
value class GlideBase(
    val startPostponedEnterTransition: () -> Unit
) {
    @SuppressLint("CheckResult")
    fun setImageWithRequest(
        imageView: ImageView,
        url: String,
        needCrop: Boolean = false,
        needCircleCrop: Boolean = false,
        needOriginal: Boolean = false
    ) {
        val urlSet = if (url == "null" || url.isEmpty()) {
            R.drawable.ic_default_photo
        } else url
        val glide = Glide.with(imageView)
            .load(urlSet)
            .placeholder(R.drawable.ic_default_photo)
            .listener(primaryRequestListener)
        if (needCrop) glide.centerCrop()
        if (needCircleCrop) glide.circleCrop()
        if (needOriginal) glide.override(Target.SIZE_ORIGINAL)
        glide.into(imageView)
    }

    private val primaryRequestListener: RequestListener<Drawable>
        get() = object : RequestListener<Drawable> {
            @SuppressLint("CheckResult")
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