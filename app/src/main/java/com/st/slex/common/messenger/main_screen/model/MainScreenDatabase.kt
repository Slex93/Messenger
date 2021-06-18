package com.st.slex.common.messenger.main_screen.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.st.slex.common.messenger.main_screen.model.base.MainMessage

class MainScreenDatabase {

    fun getMainList(): LiveData<List<MainMessage>> {

        val list = mutableListOf<MainMessage>()
        val list1 = MainMessage("1", "Helen", "Hello, Alex", "12:30")
        val list2 = MainMessage("1", "Bob", "Wow", "11:30")
        val list3 = MainMessage("1", "Anya", "I don't know", "10:30")
        val list4 = MainMessage("1", "Olga", "May be it is not a good idea", "09:30")

        list.add(list1)
        list.add(list2)
        list.add(list3)
        list.add(list4)

        return liveData { this.emit(list) }
    }

}