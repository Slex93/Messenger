package st.slex.messenger.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide

class CustomImageView : androidx.appcompat.widget.AppCompatImageView, AbstractView.Image {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun load(url: String) {
        this.transitionName = url
        if (url.isNotEmpty())
            Glide.with(this).load(url).circleCrop().into(this)
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}