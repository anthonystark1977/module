package com.attractive.deer.ext

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Context.hideKeyBoard(view: View) =
    (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        view.windowToken,
        0
    )


fun Context.showKeyBoard(view: View) =
    (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
        view,
        0
    )