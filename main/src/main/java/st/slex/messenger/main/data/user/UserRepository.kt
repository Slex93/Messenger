package st.slex.messenger.main.data.user

import android.net.Uri
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
import st.slex.core.FirebaseConstants.CHILD_STATE
import st.slex.core.FirebaseConstants.CHILD_URL
import st.slex.core.FirebaseConstants.CHILD_USERNAME
import st.slex.core.FirebaseConstants.FOLDER_PROFILE_IMAGE
import st.slex.core.FirebaseConstants.NODE_USER
import st.slex.core.FirebaseConstants.NODE_USERNAME
import st.slex.core.Resource
import st.slex.messenger.main.data.core.CompleteTaskListener
import st.slex.messenger.main.data.core.ValueSnapshotListener
import javax.inject.Inject

interface UserRepository {

    suspend fun getUserState(uid: String): Flow<Resource<String>>
    suspend fun getUser(uid: String? = null): Flow<Resource<UserData>>
    suspend fun saveUsername(username: String): Flow<Resource<Nothing?>>
    suspend fun saveImage(uri: Uri): Flow<Resource<Nothing?>>
    suspend fun getUserUrl(uid: String): Flow<Resource<String>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val listener: ValueSnapshotListener,
        private val taskListener: CompleteTaskListener,
        private val databaseReference: DatabaseReference,
        private val storageReference: StorageReference,
        private val user: FirebaseUser
    ) : UserRepository {

        override suspend fun getUserState(uid: String): Flow<Resource<String>> = callbackFlow {
            val reference = databaseReference.child(NODE_USER).child(uid).child(CHILD_STATE)
            val listener = listener.singleEventListener(String::class) { trySendBlocking(it) }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun getUserUrl(uid: String): Flow<Resource<String>> = callbackFlow {
            val reference = databaseReference.child(NODE_USER).child(uid).child(CHILD_URL)
            val listener = listener.singleEventListener(String::class) { trySendBlocking(it) }
            reference.addListenerForSingleValueEvent(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun getUser(uid: String?): Flow<Resource<UserData>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(uid ?: user.uid)
            val listener = listener.singleEventListener(UserData.Base::class) {
                trySendBlocking(it)
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun saveUsername(username: String): Flow<Resource<Nothing?>> =
            callbackFlow {
                val reference = databaseReference.child(NODE_USERNAME)
                val listener = saveUserListener(username) { trySendBlocking(it) }
                reference.addValueEventListener(listener)
                awaitClose { reference.removeEventListener(listener) }
            }

        override suspend fun saveImage(uri: Uri): Flow<Resource<Nothing?>> = callbackFlow {
            val referenceUser = databaseReference.child(NODE_USER).child(user.uid).child(CHILD_URL)
            val referenceStorage = storageReference.child(FOLDER_PROFILE_IMAGE).child(user.uid)
            val task = referenceStorage.putFile(uri)

            val userListener = taskListener.onCompleteListener<Void>({
                trySendBlocking(Resource.Success(null))
            }, {
                trySendBlocking(it)
            })

            val downloadListener = taskListener.onCompleteListener<Uri>({
                referenceUser.setValue(it.toString()).addOnCompleteListener(userListener)
            }, {
                trySendBlocking(it)
            })

            val fileListener = taskListener.onCompleteListener<UploadTask.TaskSnapshot>({
                referenceStorage.downloadUrl.addOnCompleteListener(downloadListener)
            }, {
                trySendBlocking(it)
            })

            task.addOnCompleteListener(fileListener)
            awaitClose { task.removeOnCompleteListener(fileListener) }
        }

        private inline fun saveUserListener(
            username: String,
            crossinline function: (Resource<Nothing?>) -> Unit
        ) = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfUsernames = snapshot.children.filter {
                    it.key != user.uid
                }.map { it.value }
                if (listOfUsernames.contains(username)) {
                    function(Resource.Failure(Exception("Take another username")))
                } else {
                    val taskUser = userReference.setValue(username)
                    val taskUsername = userNameReference.setValue(username)
                    val usernameCompleteListener =
                        taskListener.onCompleteListener<Void> { function(it) }
                    val userCompleteListener = taskListener.onCompleteListener<Void>({
                        taskUsername.addOnCompleteListener(usernameCompleteListener)
                    }, { failResult -> function(failResult) })
                    taskUser.addOnCompleteListener(userCompleteListener)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                function(Resource.Failure(error.toException()))
            }
        }


        private val userReference: DatabaseReference by lazy {
            databaseReference.child(NODE_USER).child(user.uid).child(CHILD_USERNAME)
        }

        private val userNameReference: DatabaseReference by lazy {
            databaseReference.child(NODE_USERNAME).child(user.uid)
        }
    }
}