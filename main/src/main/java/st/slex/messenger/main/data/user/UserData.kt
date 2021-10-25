package st.slex.messenger.main.data.user

interface UserData {

    fun id(): String
    fun phone(): String
    fun username(): String
    fun url(): String
    fun bio(): String
    fun fullName(): String
    fun state(): String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val username: String = "",
        val url: String = "",
        val bio: String = "",
        val full_name: String = "",
        val state: String = "",
    ) : UserData {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun username(): String = username
        override fun url(): String = url
        override fun bio(): String = bio
        override fun fullName(): String = full_name
        override fun state(): String = state
    }
}