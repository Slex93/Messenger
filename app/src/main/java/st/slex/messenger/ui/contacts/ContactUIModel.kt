package st.slex.messenger.ui.contacts

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import st.slex.common.messenger.R

interface ContactUIModel {

    fun bind(
        phoneTextView: TextView,
        nameTextView: TextView,
        imageView: ImageView,
        card: MaterialCardView
    )

    val getId: String

    //fun copy(newUrl: String): ContactUIModel
    fun copy(
        id: String? = null,
        phone: String? = null,
        full_name: String? = null,
        url: String? = null
    ): ContactUIModel

    fun openChat(function: (CardView, String) -> Unit)

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
        val url: String = ""
    ) : ContactUIModel {

        override fun copy(
            id: String?,
            phone: String?,
            full_name: String?,
            url: String?
        ): ContactUIModel = copy(
            id = id ?: this.id,
            phone = phone ?: this.phone,
            full_name = full_name ?: this.full_name,
            url = url ?: this.url
        )

        override val getId: String
            get() = id

        private var _cardView: MaterialCardView? = null
        private val cardView: MaterialCardView get() = checkNotNull(_cardView)

        override fun bind(
            phoneTextView: TextView,
            nameTextView: TextView,
            imageView: ImageView,
            card: MaterialCardView
        ) {
            phoneTextView.text = phone
            nameTextView.text = full_name
            val urlSet = if (url.isEmpty()) {
                R.drawable.test_image
            } else url
            card.transitionName = id
            _cardView = card
            Glide.with(imageView).load(urlSet).into(imageView)
        }

        override fun openChat(function: (CardView, String) -> Unit) = function(cardView, url)
    }
}