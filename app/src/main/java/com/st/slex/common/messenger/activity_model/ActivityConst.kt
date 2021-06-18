package com.st.slex.common.messenger.activity_model

import com.google.firebase.auth.FirebaseAuth

lateinit var AUTH: FirebaseAuth
lateinit var USER: User
lateinit var CURRENT_UID: String

const val NODE_USER = "user"
const val NODE_USERNAME = "username"
const val NODE_PHONE = "phone"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_NAME = "name"
const val CHILD_STATE = "state"