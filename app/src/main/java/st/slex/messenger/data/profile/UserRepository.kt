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
import st.slex.messenger.core.Resource
import st.slex.messenger.utilites.*
import javax.inject.Inject

interface UserRepository {

    suspend fun getUserState(uid: String): Flow<Resource<String>>
    suspend fun getUser(uid: String): Flow<Resource<UserData>>
    suspend fun saveUsername(username: String): Flow<Resource<Nothing?>>
    suspend fun saveImage(uri: Uri): Flow<Resource<Nothing?>>
    suspend fun getCurrentUser(): Flow<Resource<UserData>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val storageReference: StorageReference,
        private val user: FirebaseUser
    ) : UserRepository {

        override suspend fun getUserState(uid: String): Flow<Resource<String>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(uid)
                .child(CHILD_STATE)
            val listener = listener<String> {
                trySendBlocking(it)
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun getCurrentUser(): Flow<Resource<UserData>> = getUser(user.uid)

        override suspend fun getUser(uid: String): Flow<Resource<UserData>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(uid)
            val listener = listener<UserData.Base> {
                trySendBlocking(it)
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        private inline fun <reified T> listener(
            crossinline function: (Resource<T>) -> Unit
        ) = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                function(Resource.Success(snapshot.getValue(T::class.java)!!))
            }

            override fun onCancelled(error: DatabaseError) {
                function(Resource.Failure(error.toException()))
            }
        }

        override suspend fun saveUsername(username: String): Flow<Resource<Nothing?>> =
            callbackFlow {
                val listener = saveUserListener(username) {
                    trySendBlocking(it)
                }
                userReference.addValueEventListener(listener)
                awaitClose { userReference.removeEventListener(listener) }
            }

        override suspend fun saveImage(uri: Uri): Flow<Resource<Nothing?>> = callbackFlow {
            val referenceUser = databaseReference.child(NODE_USER).child(user.uid).child(CHILD_URL)
            val referenceStorage = storageReference.child(FOLDER_PROFILE_IMAGE).child(user.uid)
            val task = referenceStorage.putFile(uri)

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

            task.addOnCompleteListener(fileListener)
            awaitClose { task.removeOnCompleteListener(fileListener) }
        }

        private inline fun <T> listener(
            crossinline success: (T) -> Unit,
            crossinline failure: (Resource<Nothing?>) -> Unit
        ) = OnCompleteListener<T> { p0 ->
            if (p0.isSuccessful) {
                success(p0.result!!)
            } else {
                failure(Resource.Failure(p0.exception!!))
            }
        }

        private val userReference by lazy { databaseReference.child(NODE_USERNAME) }

        private inline fun saveUserListener(
            username: String,
            crossinline function: (Resource<Nothing?>) -> Unit
        ) = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfUsernames = snapshot.children.filter {
                    it.key != user.uid
                }.map {
                    it.value
                }
                if (listOfUsernames.contains(username)) {
                    function(Resource.Failure(Exception("Take another username")))
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
                                    function(Resource.Success(null))
                                } else {
                                    function(Resource.Failure(responseUsername.exception!!))
                                }
                            }
                        } else {
                            function(Resource.Failure(responseUser.exception!!))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                function(Resource.Failure(error.toException()))
            }
        }

    }
}