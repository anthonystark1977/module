package com.tony.stark.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton

@Singleton
class SecureSharedPreferences(
    private val mSharedPref: SharedPreferences,
    private val context: Context
) {

    fun contains(key: String) = mSharedPref.contains(key)

    fun get(key: String, defaultValue: Boolean): Boolean = getInternal(key, defaultValue)
    fun get(key: String, defaultValue: Int): Int = getInternal(key, defaultValue)
    fun get(key: String, defaultValue: Long): Long = getInternal(key, defaultValue)
    fun get(key: String, defaultValue: String): String = getInternal(key, defaultValue)

    private fun <T : Any> getInternal(key: String, defaultValue: T): T {
        var data: String? =mSharedPref.getString(key,"")
        if(data.isNullOrEmpty())
            return defaultValue
        val value = DataSecurityUtil.decryptText(context, data)
        @Suppress("PlatformExtensionReceiverOfInline", "UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
        return when(defaultValue) {
            is Boolean -> value?.toBoolean()
            is Int     -> value?.toInt()
            is Long    -> value?.toLong()
            is String  -> value
            else -> throw IllegalArgumentException("defaultValue only could be one of these types: Boolean, Int, Long, String")
        } as T
    }


    fun put(key: String, value: Boolean) = putInternal(key, value)
    fun put(key: String, value: Int) = putInternal(key, value)
    fun put(key: String, value: Long) = putInternal(key, value)
    fun put(key: String, value: String) = putInternal(key, value)

    private fun putInternal(key: String, value: Any?) {
        try {
            mSharedPref.edit().run {
                if (value == null) {
                    remove(key)
                } else {
                    putString(key, DataSecurityUtil.encryptText(context, value.toString()))
                }
                apply()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    companion object {
        fun wrap(sharedPref: SharedPreferences,context: Context) = SecureSharedPreferences(sharedPref,context)
        fun Context.getPreferences(name:String,mode:Int): SharedPreferences = getSharedPreferences(name, mode)

        const val PREFERENCE_GLOVV = "PREF_GLOVV"
        const val KEY_PUSH_TOKEN = "pushToken"
        const val KEY_FOLLOWING_LIST = "followingList"
        const val KEY_UUID = "uuid"
        const val KEY_DEVICE_TOKEN = "deviceToken"
        const val KEY_LOGIN_DATA = "loginData"
        const val KEY_STYLE_META_DATA = "styleMetaData"
        const val KEY_STYLE_DAILY_META_DATA = "styleDailyMetaData"
        const val KEY_STYLE_PLAY_META_DATA = "stylePlayMetaData"
        const val KEY_STORE_MALE_META_DATA = "storeMaleMetaData"
        const val KEY_STORE_FEMALE_META_DATA = "storeFemaleMetaData"
        const val KEY_COLOR_DATA = "colorData"
        const val KEY_REPORT_CATEGORY_DATA = "reportCategoryData"
        const val KEY_SEARCH_HISTORY = "searchHistory"
        const val KEY_DAILY_DETAIL = "dailyDetail"
        const val KEY_PLAY_DETAIL = "playDetail"
    }
}