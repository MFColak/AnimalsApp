package com.android.mfcolak.animalsapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

val PERMISSIONS_SEND_SMS = 123
fun ImageView.downloadImage(url: String?, circularProgressDrawable: CircularProgressDrawable) {

    val options = RequestOptions().placeholder(circularProgressDrawable)
    Glide.with(this.context).setDefaultRequestOptions(options).load(url).into(this)
}

fun createPlaceholder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 32f
        start()
    }


}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.downloadImage(url, createPlaceholder(view.context))
}