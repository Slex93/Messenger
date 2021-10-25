package st.slex.messenger.main.data.core

import com.google.firebase.database.ValueEventListener
import st.slex.messenger.core.Resource
import javax.inject.Inject
import kotlin.reflect.KClass

interface ValueSnapshotListener {

    fun <T : Any> singleEventListener(
        type: KClass<T>,
        eventResult: (Resource<T>) -> Unit
    ): ValueEventListener

    fun <T : Any> multipleEventListener(
        type: KClass<T>,
        eventResult: (Resource<List<T>>) -> Unit
    ): ValueEventListener

    class Base @Inject constructor() : ValueSnapshotListener {

        override fun <T : Any> singleEventListener(
            type: KClass<T>,
            eventResult: (Resource<T>) -> Unit
        ) = ValueListener(
            cancelled = { eventResult(it) },
            dataChange = { snapshot ->
                val result: T = checkNotNull(snapshot.getValue(type.java))
                eventResult(Resource.Success(result))
            }
        )

        override fun <T : Any> multipleEventListener(
            type: KClass<T>,
            eventResult: (Resource<List<T>>) -> Unit
        ) = ValueListener(
            cancelled = { eventResult(it) },
            dataChange = { snapshot ->
                val result: List<T> = snapshot.children.mapNotNull {
                    it.getValue(type.java)
                }
                eventResult(Resource.Success(result))
            })
    }
}
