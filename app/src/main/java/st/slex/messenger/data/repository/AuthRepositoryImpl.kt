package st.slex.messenger.data.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st.slex.messenger.activity_model.ActivityConst
import st.slex.messenger.activity_model.User
import st.slex.messenger.auth.model.base.AuthUser
import st.slex.messenger.utilites.result.AuthResult
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl : AuthRepository {

    private var _user = MutableLiveData<AuthResult<AuthUser>>()
    val user: LiveData<AuthResult<AuthUser>> get() = _user

    suspend fun signInWithPhone(phone: String, activity: Activity) =
        withContext(Dispatchers.IO) {
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

    private fun makeCallback(phone: String) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                ActivityConst.AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val authUser = AuthUser(id = ActivityConst.CURRENT_UID, phoneNumber = phone)
                        ActivityConst.AUTH_USER = authUser
                        _user.value = AuthResult.Success(authUser)
                        authUser(authUser)
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                _user.value = AuthResult.Failure(p0.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                val authUser = AuthUser(id = id, phoneNumber = phone)
                ActivityConst.AUTH_USER = authUser
                _user.value = AuthResult.Send(authUser)
            }
        }

    private fun authUser(authUser: AuthUser) {
        val id = ActivityConst.AUTH.currentUser?.uid.toString()
        val phone = authUser.phoneNumber
        val dateMap = mutableMapOf<String, Any>()
        dateMap[ActivityConst.CHILD_ID] = id
        dateMap[ActivityConst.CHILD_PHONE] = phone
        ActivityConst.REF_DATABASE_ROOT.child(ActivityConst.NODE_PHONE).child(id).setValue(phone)
            .addOnSuccessListener {
                ActivityConst.REF_DATABASE_ROOT.child(ActivityConst.NODE_USER).child(id)
                    .updateChildren(dateMap)
                    .addOnSuccessListener {
                        val user =
                            User(authUser.id, authUser.phoneNumber)
                        ActivityConst.USER = user
                    }
            }
    }

}