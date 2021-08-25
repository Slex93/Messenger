package st.slex.messenger.utilites.result

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

sealed class EventResponse {
    data class Success(val snapshot: DataSnapshot) : EventResponse()
    class Cancelled(val databaseError: DatabaseError) : EventResponse()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$snapshot]"
            is Cancelled -> "Failure[exception=$databaseError]"
        }
    }
}
