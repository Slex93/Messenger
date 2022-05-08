package st.slex.messenger.auth.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.slex.messenger.auth.data.core.CoroutinesHandle.handle
import st.slex.messenger.auth.data.utils.interf.TokenUtil
import st.slex.messenger.auth.domain.interf.AuthRepository
import st.slex.messenger.core.FirebaseConstants
import st.slex.messenger.core.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val reference: Lazy<DatabaseReference>,
    private val user: Lazy<FirebaseUser>,
    private val tokenUtil: TokenUtil
) : AuthRepository {

    override suspend fun saveUser(): Flow<Resource<Void>> = flow {
        val updateUserTask = userReference.updateChildren(mapUser)
        val updatePhoneTask = phoneReference.setValue(user.get().uid)
        val updateUserResult = updateUserTask.handle()
        if (updateUserResult is Resource.Success) {
            tokenUtil.sendToken()
            emit(updatePhoneTask.handle())
        } else emit(updateUserResult)
    }

    private val userReference: DatabaseReference by lazy {
        reference.get().child(FirebaseConstants.NODE_USER).child(user.get().uid)
    }

    private val phoneReference: DatabaseReference by lazy {
        reference.get().child(FirebaseConstants.NODE_PHONE).child(user.get().phoneNumber.toString())
    }

    private val mapUser: Map<String, Any> by lazy {
        mapOf<String, Any>(
            FirebaseConstants.CHILD_ID to user.get().uid,
            FirebaseConstants.CHILD_PHONE to user.get().phoneNumber.toString()
        )
    }
}