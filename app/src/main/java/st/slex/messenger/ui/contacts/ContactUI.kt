package st.slex.messenger.ui.contacts

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import st.slex.common.messenger.R

interface ContactUI {

    fun bind(
        phoneTextView: TextView,
        nameTextView: TextView,
        imageView: ImageView,
        card: MaterialCardView
    )

    val getId: String
    val getPhone: String
    val getFullName: String

    fun copy(
        id: String? = null,
        phone: String? = null,
        full_name: String? = null,
        url: String? = null
    ): ContactUI

    fun openChat(function: (CardView, String) -> Unit)

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
        val url: String = ""
    ) : ContactUI {

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

        override fun copy(
            id: String?,
            phone: String?,
            full_name: String?,
            url: String?
        ): ContactUI = copy(
            id = id ?: this.id,
            phone = phone ?: this.phone,
            full_name = full_name ?: this.full_name,
            url = url ?: this.url
        )

        override val getId: String
            get() = id
        override val getFullName: String
            get() = full_name
        override val getPhone: String
            get() = phone
    }
}