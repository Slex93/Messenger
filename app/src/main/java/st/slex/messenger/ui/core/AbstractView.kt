package st.slex.messenger.ui.core

import st.slex.messenger.core.TextMapper

interface AbstractView {

    fun show()
    fun hide()

    interface Text : AbstractView, TextMapper.Void

    interface Image : AbstractView {

        fun load(url: String)
    }
}