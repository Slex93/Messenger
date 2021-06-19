package com.st.slex.common.messenger.activity.activity_model

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone: String = "",
    var url: String = "url",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: Any = "",

    var lastMessage: String = ""
) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}
