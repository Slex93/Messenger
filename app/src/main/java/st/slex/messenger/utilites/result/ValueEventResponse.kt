package st.slex.messenger.utilites.result

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

sealed class ValueEventResponse {
    data class Success(val snapshot: DataSnapshot) : ValueEventResponse()
    class Cancelled(val databaseError: DatabaseError) : ValueEventResponse()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$snapshot]"
            is Cancelled -> "Failure[exception=$databaseError]"
        }
    }
}
