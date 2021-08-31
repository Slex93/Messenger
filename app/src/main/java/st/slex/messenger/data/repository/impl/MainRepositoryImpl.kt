package st.slex.messenger.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.utilites.NODE_CHAT_LIST
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.base.AppChildEventListener
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : MainRepository {

    private val _listOfChats = MutableLiveData<Response<ChatListModel>>()
    override val list: LiveData<Response<ChatListModel>> get() = _listOfChats

    override suspend fun getCurrentUser(): Flow<Response<UserModel>> = callbackFlow {
        val reference = databaseReference.child(NODE_USER).child(auth.currentUser?.uid.toString())
        val listener = AppValueEventListener({ snapshot ->
            trySendBlocking(Response.Success(snapshot.getThisValue()))
        }, { exception ->
            trySendBlocking(Response.Failure(exception))
        })
        reference.addListenerForSingleValueEvent(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    override suspend fun getChatList(): Unit = withContext(Dispatchers.IO) {
        val reference =
            databaseReference.child(NODE_CHAT_LIST).child(auth.currentUser?.uid.toString())
        val listener = AppChildEventListener({ snapshot ->
            _listOfChats.value = Response.Success(snapshot.getThisValue())
        }, {
            _listOfChats.value = Response.Failure(it)
        })
        reference.addChildEventListener(listener)
    }

}