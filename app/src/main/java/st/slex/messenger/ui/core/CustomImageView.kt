package st.slex.messenger.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import st.slex.common.messenger.R

class CustomImageView : androidx.appcompat.widget.AppCompatImageView, AbstractView.Image {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun load(url: String) {
        val urlSet = if (url.isEmpty()) {
            R.drawable.test_image
        } else url
        this.transitionName = url
        Glide.with(this).load(urlSet).circleCrop().into(this)
    }

    override fun getImage(): CustomImageView = this

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}