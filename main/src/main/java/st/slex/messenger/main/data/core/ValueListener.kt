package st.slex.messenger.main.data.core

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import st.slex.messenger.core.Resource

class ValueListener(
    val cancelled: (Resource.Failure<Nothing>) -> Unit,
    val dataChange: (snapshot: DataSnapshot) -> Unit
) : ValueEventListener {

    override fun onCancelled(error: DatabaseError) =
        cancelled(Resource.Failure(error.toException()))

    override fun onDataChange(snapshot: DataSnapshot) = try {
        dataChange(snapshot)
    } catch (exception: Exception) {
        cancelled(Resource.Failure(exception))
    }
}