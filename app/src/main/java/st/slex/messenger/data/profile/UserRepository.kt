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
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.utilites.*
import javax.inject.Inject

interface UserRepository {
    suspend fun getUser(uid: String): Flow<DataResult<UserData>>
    suspend fun saveUsername(username: String): Flow<DataResult<*>>
    suspend fun saveImage(uri: Uri): Flow<DataResult<*>>
    suspend fun getCurrentUser(): Flow<DataResult<UserData>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val storageReference: StorageReference,
        private val user: FirebaseUser
    ) : UserRepository {

        override suspend fun getCurrentUser(): Flow<DataResult<UserData>> = getUser(user.uid)

        override suspend fun getUser(uid: String): Flow<DataResult<UserData>> = callbackFlow {
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
            crossinline function: (DataResult<UserData>) -> Unit
        ) = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) =
                function(DataResult.Success(snapshot.getValue(UserData.Base::class.java)!!))

            override fun onCancelled(error: DatabaseError) =
                function(DataResult.Failure(error.toException()))
        }

        override suspend fun saveImage(uri: Uri): Flow<DataResult<*>> = callbackFlow {
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
            crossinline failure: (DataResult<*>) -> Unit
        ) = OnCompleteListener<T> { p0 ->
            if (p0.isSuccessful) {
                success(p0.result!!)
            } else {
                failure(DataResult.Failure<Nothing>(p0.exception!!))
            }
        }

        override suspend fun saveUsername(username: String): Flow<DataResult<*>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USERNAME)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfUsernames = snapshot.children.filter {
                        it.key != user.uid
                    }.map {
                        it.value
                    }
                    if (listOfUsernames.contains(username)) {
                        trySendBlocking(DataResult.Failure<Nothing>(Exception("Take another username")))
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
                                        trySendBlocking(DataResult.Success(null))
                                    } else {
                                        trySendBlocking(DataResult.Failure<Nothing>(responseUsername.exception!!))
                                    }
                                }
                            } else {
                                trySendBlocking(DataResult.Failure<Nothing>(responseUser.exception!!))
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(DataResult.Failure<Nothing>(error.toException()))
                }

            }

            reference.addValueEventListener(listener)

            awaitClose { reference.removeEventListener(listener) }
        }


    }
}