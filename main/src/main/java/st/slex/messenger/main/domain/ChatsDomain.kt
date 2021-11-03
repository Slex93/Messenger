package st.slex.messenger.main.domain

interface ChatsDomain {

    data class Base(
        val id: String? = ""
    ) : ChatsDomain
}