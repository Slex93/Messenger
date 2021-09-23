package st.slex.messenger.ui.auth.phone

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

internal object PhoneNumberUtilInstanceProvider {

    private var phoneNumberUtil: PhoneNumberUtil? = null

    fun set(phoneNumberUtil: PhoneNumberUtil) {
        this.phoneNumberUtil = phoneNumberUtil
    }

    fun get() = phoneNumberUtil
        ?: throw IllegalStateException("PhoneNumberUtil instance hasn't been initialized.")
}