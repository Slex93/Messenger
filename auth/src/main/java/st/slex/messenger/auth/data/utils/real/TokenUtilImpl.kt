package st.slex.messenger.auth.data.utils.real

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Lazy
import st.slex.messenger.auth.data.core.CoroutinesHandle.handle
import st.slex.messenger.auth.data.utils.interf.TokenUtil
import st.slex.messenger.core.FirebaseConstants
import javax.inject.Inject

class TokenUtilImpl @Inject constructor(
    private val reference: Lazy<DatabaseReference>,
    private val user: Lazy<FirebaseUser>
) : TokenUtil {

    override suspend fun sendToken() {
        val token = FirebaseMessaging.getInstance().token.handle()
        tokenReference.setValue(token).handle()
    }

    private val tokenReference: DatabaseReference by lazy {
        reference.get().child(FirebaseConstants.NODE_TOKENS).child(user.get().uid)
    }
}