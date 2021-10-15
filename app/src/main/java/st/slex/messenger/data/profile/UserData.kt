package st.slex.messenger.data.profile

interface UserData {

    val getId: String
    val getPhone: String
    val getUsername: String
    val getUrl: String
    val getBio: String
    val getFullName: String
    val getState: String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val username: String = "",
        val url: String = "",
        val bio: String = "",
        val full_name: String = "",
        val state: String = "",
    ) : UserData {

        override val getId: String = id
        override val getPhone: String = phone
        override val getUsername: String = username
        override val getUrl: String = url
        override val getBio: String = bio
        override val getFullName: String = full_name
        override val getState: String = state
    }
}