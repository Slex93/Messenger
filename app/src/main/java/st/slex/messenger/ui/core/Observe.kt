package st.slex.messenger.ui.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface Observe<T> {
    fun observe(owner: LifecycleOwner, observer: Observer<T>)
}