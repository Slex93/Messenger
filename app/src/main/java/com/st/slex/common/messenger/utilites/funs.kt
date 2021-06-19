package com.st.slex.common.messenger.utilites

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.activity.MainActivity

fun View.showPrimarySnackBar(it: String) {
    Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
}

fun Activity.restartActivity() {
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
    this.finish()
}

fun ImageView.downloadAndSet(url: String) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.girl_photo))
        .into(this)
}