package com.tony.stark.ext

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil

// val Context.layoutInflater: LayoutInflater get() = this.layoutInflater

/*
fun Activity.showToastFinish(@IdRes lytRoot: Int, @LayoutRes lytToast: Int) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastFinishBinding>(customView)
    Toast(this).apply {
        // setGravity(Gravity.BOTTOM or Gravity.CENTER,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}
fun Activity.showToastLike(@IdRes lytRoot: Int, @LayoutRes lytToast: Int, likeFlag:Boolean?) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastLikeBinding>(customView)?.apply { likeFlag?.let{flag = !likeFlag }}
    Toast(this).apply {
        setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}

fun Activity.showToastHashTagOver(@IdRes lytRoot: Int, @LayoutRes lytToast: Int) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastHashTagOverBinding>(customView)
    Toast(this).apply {
        // setGravity(Gravity.BOTTOM or Gravity.CENTER,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}
*/
