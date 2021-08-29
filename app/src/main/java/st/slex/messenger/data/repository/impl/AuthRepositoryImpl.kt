package st.slex.messenger.data.repository.impl

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
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.Const
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.Const.authUserModel
import st.slex.messenger.utilites.result.AuthResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private var _user = MutableLiveData<AuthResult<AuthUserModel>>()
    override val userModel: LiveData<AuthResult<AuthUserModel>> get() = _user

    override suspend fun signInWithPhone(phone: String, activity: Activity) =
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

    override suspend fun sendCode(id: String, code: String): Unit = withContext(Dispatchers.IO) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value =
                    AuthResult.Success(
                        AuthUserModel(
                            id = id,
                            task.result?.user?.phoneNumber.toString()
                        )
                    )
            } else {
                _user.value = AuthResult.Failure(task.exception.toString())
            }
        }
            .addOnFailureListener {
                _user.value = AuthResult.Failure(it.toString())
            }
    }

    override suspend fun authUser(authUserModel: AuthUserModel): Unit =
        withContext(Dispatchers.IO) {
            val id = AUTH.currentUser?.uid.toString()
            val phone = authUserModel.phoneNumber
            val dateMap = mutableMapOf<String, Any>()
            dateMap[Const.CHILD_ID] = id
            dateMap[Const.CHILD_PHONE] = phone
            Const.REF_DATABASE_ROOT.child(Const.NODE_PHONE).child(id)
                .setValue(phone)
                .addOnSuccessListener {
                    Const.REF_DATABASE_ROOT.child(Const.NODE_USER).child(id)
                        .updateChildren(dateMap)
                        .addOnSuccessListener {
                            val user =
                                UserModel(authUserModel.id, authUserModel.phoneNumber)
                            AuthResult.Success(user)
                            Const.USER = user
                        }
                }
        }

    private fun makeCallback(phone: String) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val authUser =
                            AuthUserModel(id = Const.CURRENT_UID, phoneNumber = phone)
                        authUserModel = authUser
                        _user.value = AuthResult.Success(authUser)
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                _user.value = AuthResult.Failure(p0.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                val authUser = AuthUserModel(id = id, phoneNumber = phone)
                authUserModel = authUser
                _user.value = AuthResult.Send(authUser)
            }
        }

}