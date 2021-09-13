package st.slex.messenger.core

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(
    val success: (DataSnapshot) -> Unit,
    val failure: (Exception) -> Unit
) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        success(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        failure(error.toException())
    }
}