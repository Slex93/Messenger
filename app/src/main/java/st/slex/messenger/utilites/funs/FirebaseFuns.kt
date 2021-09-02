package st.slex.messenger.utilites.funs

import com.google.firebase.database.DataSnapshot

inline fun <reified T> DataSnapshot.getThisValue(): T = getValue(T::class.java) as T

