package com.lovely.deer.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets

class StatusBarLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mStatusBarHeight = 0
    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        mStatusBarHeight = insets.systemWindowInsetTop
        return insets.consumeSystemWindowInsets()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStatusBarHeight)
    }

    init {
        systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}