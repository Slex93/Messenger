package com.st.slex.common.messenger.utilites

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showPrimarySnackBar(it: String) {
    Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
}