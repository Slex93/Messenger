package st.slex.messenger.main.data.core

import com.google.android.gms.tasks.OnCompleteListener
import st.slex.messenger.core.Resource
import javax.inject.Inject

interface CompleteTaskListener {

    fun <T> onCompleteListener(
        function: (Resource<Nothing?>) -> Unit
    ): OnCompleteListener<T>

    fun <T> onCompleteListener(
        success: (T) -> Unit,
        failure: (Resource<Nothing?>) -> Unit
    ): OnCompleteListener<T>

    class Base @Inject constructor() : CompleteTaskListener {

        override fun <T> onCompleteListener(
            function: (Resource<Nothing?>) -> Unit
        ): OnCompleteListener<T> = OnCompleteListener<T> { task ->
            if (task.isSuccessful) {
                function(Resource.Success(null))
            } else {
                function(Resource.Failure(task.exception!!))
            }
        }

        override fun <T> onCompleteListener(
            success: (T) -> Unit,
            failure: (Resource<Nothing?>) -> Unit
        ): OnCompleteListener<T> = OnCompleteListener<T> { p0 ->
            if (p0.isSuccessful) {
                success(p0.result!!)
            } else {
                failure(Resource.Failure(p0.exception!!))
            }
        }
    }
}