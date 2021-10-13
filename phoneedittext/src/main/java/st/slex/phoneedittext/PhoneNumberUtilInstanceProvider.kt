package st.slex.phoneedittext

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

internal object PhoneNumberUtilInstanceProvider {

    private var phoneNumberUtil: PhoneNumberUtil? = null

    fun set(phoneNumberUtil: PhoneNumberUtil) {
        PhoneNumberUtilInstanceProvider.phoneNumberUtil = phoneNumberUtil
    }

    fun get() = phoneNumberUtil
        ?: throw IllegalStateException("PhoneNumberUtil instance hasn't been initialized.")
}