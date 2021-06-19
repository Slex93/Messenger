package com.st.slex.common.messenger.auth.model

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.st.slex.common.messenger.activity.activity_model.*
import com.st.slex.common.messenger.auth.model.base.AuthUser
import java.util.concurrent.TimeUnit

class AuthRepository {

    val callbackReturnStatus = MutableLiveData<String>()

    fun initPhoneNumber(phone: String, activity: Activity) {
        val callback = makeCallback(phone)
        val phoneOptions = PhoneAuthOptions
            .newBuilder(FirebaseAuth.getInstance())
            .setActivity(activity)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
    }

    fun postCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(AUTH_USER.id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authUser()
                callbackReturnStatus.value = "success"
            }
        }
            .addOnFailureListener {
                callbackReturnStatus.value = it.toString()
            }
    }

    private fun authUser() {
        val id = AUTH_USER.id
        val phone = AUTH_USER.phoneNumber
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_ID] = id
        dateMap[CHILD_PHONE] = phone
        Log.i("AUTH:::", phone)
        Log.i("AUTH::", id)
        REF_DATABASE_ROOT.child(NODE_PHONE).child(id).setValue(phone)
            .addOnSuccessListener {
                REF_DATABASE_ROOT.child(NODE_USER).child(id).updateChildren(dateMap)
                    .addOnSuccessListener {
                        val user = User(AUTH_USER.id, AUTH_USER.phoneNumber)
                        USER = user
                    }
            }
    }

    private fun makeCallback(phone: String) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callbackReturnStatus.value = "success"
                        val id = AUTH.currentUser?.uid.toString()
                        val authUser = AuthUser(id = id, phoneNumber = phone)
                        AUTH_USER = authUser
                        authUser()
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                callbackReturnStatus.value = p0.toString()
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                val authUser = AuthUser(id = id, phoneNumber = phone)
                AUTH_USER = authUser
                callbackReturnStatus.value = "send"
            }
        }
}