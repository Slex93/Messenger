package st.slex.messenger.data.repository.impl

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.funs.callback
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.funs.signInWithPhone
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.ValueEventResponse
import st.slex.messenger.utilites.result.VoidResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth,
    private val service: DatabaseSnapshot
) : AuthRepository {

    override suspend fun signInWithPhone(phone: String, activity: Activity) = callbackFlow {
        val callback = callback({ credential ->
            auth.signInWithPhone(credential, {
                trySendBlocking(AuthResponse.Success).isSuccess
            }, {
                trySendBlocking(AuthResponse.Failure(it)).isFailure
            })
        }, { exception ->
            trySendBlocking(AuthResponse.Failure(exception)).isFailure
        }, { id, _ ->
            trySendBlocking(AuthResponse.Send(id)).isSuccess
        })
        val phoneOptions = PhoneAuthOptions
            .newBuilder(FirebaseAuth.getInstance())
            .setActivity(activity)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()
        awaitClose { PhoneAuthProvider.verifyPhoneNumber(phoneOptions) }
    }

    override suspend fun sendCode(id: String, code: String) = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(id, code)
        val event = auth.signInWithPhone(credential, {
            trySendBlocking(AuthResponse.Success)
        }, {
            trySendBlocking(AuthResponse.Failure(it))
        })
        awaitClose { event }
    }

    @InternalCoroutinesApi
    override suspend fun authUser(): Flow<VoidResponse> = callbackFlow {
        val id = auth.currentUser?.uid.toString()
        service.singleValueEventFlow(
            databaseReference
                .child(NODE_USER)
                .child(id)
        ).collect(authCollector(
            id,
            { trySendBlocking(VoidResponse.Success) },
            { trySendBlocking(VoidResponse.Failure(it)) }
        ))
    }

    private inline fun authCollector(
        id: String,
        crossinline success: () -> Unit,
        crossinline failure: (Exception) -> Unit
    ) = object : FlowCollector<ValueEventResponse> {
        override suspend fun emit(value: ValueEventResponse) {
            val phone = auth.currentUser?.phoneNumber.toString()
            when (value) {
                is ValueEventResponse.Success -> {
                    val user = value.snapshot.getThisValue<UserModel>()
                    val username = if (user.username.isNullOrEmpty()) {
                        id
                    } else user.username
                    val mapUser = mapOf(
                        CHILD_ID to id,
                        CHILD_PHONE to phone,
                        CHILD_USERNAME to username
                    )
                    databaseReference.child(NODE_PHONE).child(id)
                        .setValue(phone)
                        .addOnSuccessListener {
                            databaseReference.child(NODE_USER).child(id).updateChildren(mapUser)
                                .addOnSuccessListener {
                                    success()
                                }
                            databaseReference.child(NODE_USERNAME).child(id).setValue(username)
                        }
                }
                is ValueEventResponse.Cancelled -> {
                    failure(value.databaseError.toException())
                }
            }
        }
    }

}