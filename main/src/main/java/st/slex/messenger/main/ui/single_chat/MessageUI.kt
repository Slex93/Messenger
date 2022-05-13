package st.slex.messenger.main.ui.single_chat

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import st.slex.messenger.main.utilites.funs.convertToTime

interface MessageUI {

    fun bindMessage(
        hideLayout: ViewGroup,
        showLayout: ViewGroup,
        messageTextView: TextView,
        timeStampTextView: TextView
    )

    fun getId(): String

    data class Base(
        val from: String = "",
        val message: String = "",
        val timestamp: String = ""
    ) : MessageUI {

        override fun getId(): String = from

        override fun bindMessage(
            hideLayout: ViewGroup,
            showLayout: ViewGroup,
            messageTextView: TextView,
            timeStampTextView: TextView
        ) {
            showLayout.visibility = View.VISIBLE
            hideLayout.visibility = View.GONE
            messageTextView.text = message
            timeStampTextView.text = timestamp.convertToTime()
        }
    }
}