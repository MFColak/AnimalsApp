package com.android.mfcolak.animalsapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.mfcolak.animalsapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.downloadImage(url: String?, placeholder: CircularProgressDrawable){

    val options = RequestOptions().placeholder(placeholder).error(R.mipmap.ic_launcher)
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}

fun createPlaceholder(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 6f
        centerRadius = 32f
        start()
    }

}

@BindingAdapter("android:downloadImage")
fun loadImage(view: ImageView, url: String?){
    view.downloadImage(url, createPlaceholder(view.context))
}