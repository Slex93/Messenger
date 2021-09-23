package st.slex.messenger.ui.auth.phone

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import st.slex.common.messenger.R

open class PhoneTextInputLayout : TextInputLayout {

    private var showFlag = true
    private var flagSize = 0f

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            with(
                context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.PhoneTextInputLayout,
                    0,
                    0
                )
            ) {
                try {
                    showFlag = getBoolean(R.styleable.PhoneTextInputLayout_phone_showFlag, true)
                    val flagSizeFromAttr =
                        getDimension(R.styleable.PhoneTextInputLayout_phone_flagSize, 0f)
                    flagSize = flagSizeFromAttr.takeIf { it > 0 }
                        ?: resources.getDimension(R.dimen.phone_default_flag_size)
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (showFlag) watchPhoneNumber()
    }

    private fun watchPhoneNumber() {
        val phonemojiEditText = editText as? PhoneTextInputEditText
        checkNotNull(phonemojiEditText) { "PhonemojiTextInputLayout requires a PhonemojiTextInputEditText child" }
        PhoneHelper.watchPhoneNumber(phonemojiEditText) {
            startIconDrawable = TextDrawable(it, flagSize)
        }
    }
}