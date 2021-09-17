package st.slex.messenger.domain.user

interface UserDomain {

    fun id(): String
    fun phone(): String
    fun username(): String
    fun url(): String
    fun bio(): String
    fun fullName(): String
    fun state(): String

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val username: String = "",
        private val url: String = "",
        private val bio: String = "",
        private val full_name: String = "",
        private val state: String = "",
    ) : UserDomain {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun username(): String = username
        override fun url(): String = url
        override fun bio(): String = bio
        override fun fullName(): String = full_name
        override fun state(): String = state
    }
}