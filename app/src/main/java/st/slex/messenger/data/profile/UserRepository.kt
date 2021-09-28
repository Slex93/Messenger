package st.slex.messenger.data.profile

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.core.VoidResult
import st.slex.messenger.utilites.*
import javax.inject.Inject

interface UserRepository {
    suspend fun getUser(uid: String): Flow<UserDataResult>
    suspend fun saveUsername(username: String): Flow<VoidResult>
    suspend fun saveImage(uri: Uri): Flow<VoidResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val storageReference: StorageReference,
        private val user: FirebaseUser
    ) : UserRepository {

        override suspend fun getUser(uid: String): Flow<UserDataResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(uid)
            val listener = getUserEventListener {
                trySendBlocking(it)
            }
            reference.addListenerForSingleValueEvent(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        private inline fun getUserEventListener(
            crossinline function: (UserDataResult) -> Unit
        ) = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) =
                function(UserDataResult.Success(snapshot.getValue(UserData.Base::class.java)!!))

            override fun onCancelled(error: DatabaseError) =
                function(UserDataResult.Failure(error.toException()))
        }

        override suspend fun saveImage(uri: Uri): Flow<VoidResult> = callbackFlow {
            val referenceUser = databaseReference.child(NODE_USER).child(user.uid).child(CHILD_URL)
            val referenceStorage = storageReference.child(FOLDER_PROFILE_IMAGE).child(user.uid)

            val userListener = listener<Void>({
                trySendBlocking(it)
            }, {
                trySendBlocking(it)
            })

            val downloadListener = listener<Uri>({
                referenceUser.setValue(it.toString()).addOnCompleteListener(userListener)
            }, {
                trySendBlocking(it)
            })

            val fileListener = listener<UploadTask.TaskSnapshot>({
                referenceStorage.downloadUrl.addOnCompleteListener(downloadListener)
            }, {
                trySendBlocking(it)
            })

            referenceStorage.putFile(uri).addOnCompleteListener(fileListener)

            awaitClose { }
        }

        private inline fun <T> listener(
            crossinline success: (T) -> Unit,
            crossinline failure: (VoidResult) -> Unit
        ) = OnCompleteListener<T> { p0 ->
            if (p0.isSuccessful) {
                success(p0.result!!)
            } else {
                failure(VoidResult.Failure(p0.exception!!))
            }
        }

        override suspend fun saveUsername(username: String): Flow<VoidResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USERNAME)
            val listener = AppValueEventListener({ snapshotUsernames ->
                val listOfUsernames = snapshotUsernames.children.filter {
                    it.key != user.uid
                }.map {
                    it.value
                }
                if (listOfUsernames.contains(username)) {
                    trySendBlocking(VoidResult.Failure(Exception("Take another username")))
                } else {
                    val taskUser = databaseReference
                        .child(NODE_USER)
                        .child(user.uid)
                        .child(CHILD_USERNAME)
                        .setValue(username)
                    val taskUsername = databaseReference
                        .child(NODE_USERNAME)
                        .child(user.uid)
                        .setValue(username)

                    taskUser.addOnCompleteListener { responseUser ->
                        if (responseUser.isSuccessful) {
                            taskUsername.addOnCompleteListener { responseUsername ->
                                if (responseUsername.isSuccessful) {
                                    trySendBlocking(VoidResult.Success)
                                } else {
                                    trySendBlocking(VoidResult.Failure(responseUsername.exception!!))
                                }
                            }
                        } else {
                            trySendBlocking(VoidResult.Failure(responseUser.exception!!))
                        }
                    }
                }

            }, {
                trySendBlocking(VoidResult.Failure(it))
            })

            reference.addValueEventListener(listener)

            awaitClose { reference.removeEventListener(listener) }
        }


    }
}