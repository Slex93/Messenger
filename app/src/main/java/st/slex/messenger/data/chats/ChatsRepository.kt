package st.slex.messenger.data.chats

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {


    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ChatsRepository {


    }
}