package st.slex.messenger.utilites.result

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

sealed class ChildEventResponse {
    data class Added(val snapshot: DataSnapshot, val previousChildName: String?) :
        ChildEventResponse()

    data class Changed(val snapshot: DataSnapshot, val previousChildName: String?) :
        ChildEventResponse()

    data class Removed(val snapshot: DataSnapshot) : ChildEventResponse()
    data class Moved(val snapshot: DataSnapshot, val previousChildName: String?) :
        ChildEventResponse()

    class Cancelled(val databaseError: DatabaseError) : ChildEventResponse()

    override fun toString(): String {
        return when (this) {
            is Added -> "Success[snapshot=$snapshot, previousChildName=$previousChildName]"
            is Changed -> "Success[snapshot=$snapshot, previousChildName=$previousChildName]"
            is Removed -> "Success[snapshot=$snapshot]"
            is Moved -> "Success[snapshot=$snapshot, previousChildName=$previousChildName]"
            is Cancelled -> "Failure[exception=$databaseError]"
        }
    }
}