package com.lovely.deer.ext

import okhttp3.ResponseBody
import timber.log.Timber

const val OK = 200
const val NO_CONTENT = 204
const val BAD_REQUEST = 400
const val NOT_ACCEPTABLE = 406
const val CONFLICT = 409
const val UNAUTHORIZED = 401
const val FORBIDDEN = 403
const val INTERNAL_SERVER_ERROR = 500
const val UNKNOWN = 100000


fun ResponseBody?.loggingNetworkError(tag: String?, networkInfo: String, errorCode: Int) {
    when (errorCode) {
        BAD_REQUEST -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::BAD_REQUEST - ${this?.string()}")
        NO_CONTENT -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::NO_CONTENT - ${this?.string()}")
        NOT_ACCEPTABLE -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::NOT_ACCEPTABLE - ${this?.string()}")
        CONFLICT -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::CONFLICT - ${this?.string()}")
        UNAUTHORIZED -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::UNAUTHORIZED - ${this?.string()}")
        FORBIDDEN -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::FORBIDDEN - ${this?.string()}")
        INTERNAL_SERVER_ERROR -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::INTERNAL_SERVER_ERROR - ${this?.string()}")
        else -> Timber.e("[$tag] >> $networkInfo method invoke #[$errorCode]::UnKnownError - ${this?.string()}")
    }
}