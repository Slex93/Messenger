package st.slex.messenger.utilites.funs

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

inline fun <reified T> DataSnapshot.getThisValue(): T = getValue(T::class.java) as T

inline fun valueEventListener(
    crossinline onDataChange: (snapshot: DataSnapshot) -> Unit,
    crossinline onCancelled: (error: DatabaseError) -> Unit
) = object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot): Unit = onDataChange(snapshot)
    override fun onCancelled(error: DatabaseError): Unit = onCancelled(error)
}

inline fun childrenEventListener(
    crossinline onChildAdded: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
    crossinline onChildChanged: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
    crossinline onChildRemoved: (snapshot: DataSnapshot) -> Unit,
    crossinline onChildMoved: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
    crossinline onCancelled: (error: DatabaseError) -> Unit,
) = object : ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) =
        onChildAdded(snapshot, previousChildName)

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) =
        onChildChanged(snapshot, previousChildName)

    override fun onChildRemoved(snapshot: DataSnapshot) = onChildRemoved(snapshot)
    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) =
        onChildMoved(snapshot, previousChildName)

    override fun onCancelled(error: DatabaseError) = onCancelled(error)
}

inline fun FirebaseAuth.signInWithPhone(
    credential: PhoneAuthCredential,
    crossinline success: () -> Unit,
    crossinline failure: (Exception) -> Unit
): Task<AuthResult> =
    signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            success()
        } else {
            failure(task.exception as Exception)
        }
    }.addOnFailureListener {
        failure(it)
    }

inline fun callback(
    crossinline onVerificationCompleted: (credential: PhoneAuthCredential) -> Unit,
    crossinline onVerificationFailed: (p0: FirebaseException) -> Unit,
    crossinline onCodeSent: (id: String, token: PhoneAuthProvider.ForceResendingToken) -> Unit
) =
    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential): Unit =
            onVerificationCompleted(credential)

        override fun onVerificationFailed(p0: FirebaseException): Unit =
            onVerificationFailed(p0)

        override fun onCodeSent(
            id: String,
            token: PhoneAuthProvider.ForceResendingToken
        ): Unit = onCodeSent(id, token)
    }