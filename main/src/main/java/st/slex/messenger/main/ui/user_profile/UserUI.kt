package st.slex.messenger.main.ui.user_profile

import com.google.android.material.appbar.MaterialToolbar
import st.slex.messenger.main.ui.core.AbstractView
import st.slex.messenger.main.ui.core.CustomCardView
import st.slex.messenger.main.utilites.base.SetImageWithGlide

interface UserUI {

    fun mapMainScreen(
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
    )

    fun mapProfile(
        glide: SetImageWithGlide,
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
        bioText: AbstractView.Text,
        fullName: AbstractView.Text,
        usernameCard: AbstractView.Card,
        toolbar: MaterialToolbar
    )

    fun changeUsername(function: (CustomCardView, String) -> Unit)

    fun id(): String
    fun phone(): String
    fun username(): String
    fun url(): String
    fun bio(): String
    fun fullName(): String
    fun state(): String

    fun copy(
        id: String? = null,
        phone: String? = null,
        username: String? = null,
        url: String? = null,
        bio: String? = null,
        full_name: String? = null,
        state: String? = null
    ): UserUI

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val username: String = "",
        private val url: String = "",
        private val bio: String = "",
        private val full_name: String = "",
        private val state: String = "",
    ) : UserUI {

        private var _usernameCard: CustomCardView? = null
        private val usernameCard get() = _usernameCard!!

        override fun mapMainScreen(
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            avatar.load(url)
        }

        override fun mapProfile(
            glide: SetImageWithGlide,
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
            bioText: AbstractView.Text,
            fullName: AbstractView.Text,
            usernameCard: AbstractView.Card,
            toolbar: MaterialToolbar
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            glide.setImage(avatar.getImage(), url, needCrop = false)
            bioText.map(bio)
            fullName.map(full_name)
            usernameCard.transit(username)
            _usernameCard = usernameCard.getCard()
            toolbar.title = username
        }

        override fun changeUsername(function: (CustomCardView, String) -> Unit) =
            function(usernameCard, username)

        override fun id(): String = id
        override fun phone(): String = phone
        override fun username(): String = username
        override fun url(): String = url
        override fun bio(): String = bio
        override fun fullName(): String = full_name
        override fun state(): String = state

        override fun copy(
            id: String?,
            phone: String?,
            username: String?,
            url: String?,
            bio: String?,
            full_name: String?,
            state: String?
        ): UserUI = Base(
            id = id ?: this.id,
            phone = phone ?: this.phone,
            username = username ?: this.username,
            url = url ?: this.url,
            bio = bio ?: this.bio,
            full_name = full_name ?: this.full_name,
            state = state ?: this.state
        )
    }
}