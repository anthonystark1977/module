package com.attractive.deer.listener

import android.app.Activity
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

class BackPressCloseHandler(
        private val activity: Activity,
        @IdRes private val rootLyt: Int?,
        @LayoutRes private val toastLyt: Int?,
        private val msg:String
) {
    private var backKeyClickTime: Long = 0
    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis()
            if(rootLyt==null && toastLyt==null) Toast.makeText(activity,"한번 더 누르면 앱을 종료합니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {
            activity.finish()
        }
    }
}