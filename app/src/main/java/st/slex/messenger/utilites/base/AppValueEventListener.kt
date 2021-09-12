package st.slex.messenger.utilites.base

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@JvmInline
value class AppValueEventListener(
    val onSuccess: (DataSnapshot) -> Unit
) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.e("EventListener", error.message, error.toException())
    }
}