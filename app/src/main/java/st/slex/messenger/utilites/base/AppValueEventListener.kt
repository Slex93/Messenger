package st.slex.messenger.utilites.base

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(
    val onSuccess: (DataSnapshot) -> Unit,
    val onFailure: (Exception) -> Unit
) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        onFailure(error.toException())
    }

}