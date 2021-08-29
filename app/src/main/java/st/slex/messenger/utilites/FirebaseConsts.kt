package st.slex.messenger.utilites

import android.Manifest

/*Nodes*/
const val NODE_USER = "user"
const val NODE_USERNAME = "username"
const val NODE_PHONE = "phone"
const val NODE_PHONE_CONTACT = "phone_contacts"
const val NODE_MESSAGES = "messages"
const val NODE_MAIN_LIST = "main_list"

/*Children*/
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_STATE = "state"
const val CHILD_FULLNAME = "fullname"
const val CHILD_FROM = "from"
const val CHILD_TEXT = "text"
const val CHILD_TIMESTAMP = "timestamp"

/*Permission Const*/
const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200