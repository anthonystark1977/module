package com.lovely.deer.ext


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator


const val DURATION_ROTATE: Long = 10000


fun View.fadeOutAnimation(duration: Long) {
    val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    fadeOut.duration = duration
    fadeOut.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            // We wanna set the view to GONE, after it's fade out. so it actually disappear from the layout & don't take up space.
            visibility = View.GONE
        }
    })
    fadeOut.start()
}


fun View.fadeInAnimation(duration: Long) {
    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    fadeIn.duration = duration
    fadeIn.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)
            // We wanna set the view to VISIBLE, but with alpha 0. So it appear invisible in the layout.
            visibility = View.VISIBLE
        }
    })
    fadeIn.start()
}


fun View.rotateIndefinitely() {
    val rotate = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f)
    rotate.apply {
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.RESTART
        interpolator = LinearInterpolator()
        duration = DURATION_ROTATE
        // interpolator = AccelerateInterpolator()
    }
    rotate.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
            super.onAnimationStart(animation, isReverse)
            visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
            super.onAnimationEnd(animation, isReverse)
        }
    })
    rotate.start()
}