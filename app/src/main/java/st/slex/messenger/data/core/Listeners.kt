package st.slex.messenger.data.core

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import st.slex.messenger.core.Resource

object Listeners {

    inline fun <reified T> singleListener(
        crossinline function: (Resource<T>) -> Unit
    ) = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) =
            function(Resource.Success(snapshot.getValue(T::class.java)!!))

        override fun onCancelled(error: DatabaseError) =
            function(Resource.Failure(error.toException()))
    }

    inline fun <reified T> multipleListener(
        crossinline function: (Resource<List<T>>) -> Unit
    ): ValueEventListener = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) =
            function(Resource.Success(snapshot.children.mapNotNull { it.getValue(T::class.java)!! }))

        override fun onCancelled(error: DatabaseError) =
            function(Resource.Failure(error.toException()))
    }
}