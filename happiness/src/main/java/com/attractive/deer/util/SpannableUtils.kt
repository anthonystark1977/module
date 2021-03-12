package com.attractive.deer.util

import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.text.*
import android.text.style.*


private const val EMPTY_STRING = ""
private const val FIRST_SYMBOL = 0

fun spannable(func: () -> SpannableString) = func()

private fun span(s: CharSequence, o: Any) = getNewSpannableString(s).apply {
    setSpan(o, FIRST_SYMBOL, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

private fun getNewSpannableString(charSequence: CharSequence): SpannableString{
    return if (charSequence is String){
        SpannableString(charSequence)
    }else{
        charSequence as? SpannableString ?: SpannableString(EMPTY_STRING)
    }
}

class CustomTypefaceSpan(family: String?, private val newType: Typeface) : TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old: Typeface = paint.typeface
            oldStyle = old?.style ?: 0
            val fake = oldStyle and tf.style.inv()
            if (fake and BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.typeface = tf
        }
    }
}

operator fun SpannableString.plus(s: CharSequence) = SpannableString(TextUtils.concat(this, "", s))

fun CharSequence.setSpannableString() = span(this, Spanned.SPAN_COMPOSING)
fun CharSequence.setBold() = span(this, StyleSpan(BOLD))
fun CharSequence.setItalic() = span(this, StyleSpan(ITALIC))
fun CharSequence.setUnderline() = span(this, UnderlineSpan())
fun CharSequence.setStrike() = span(this, StrikethroughSpan())
fun CharSequence.setSuperscript() = span(this, SuperscriptSpan())
fun CharSequence.setSubscript() = span(this, SubscriptSpan())
fun CharSequence.setRelativeSize(size: Float) = span(this, RelativeSizeSpan(size))
fun CharSequence.setAbsoluteSize(size: Int) = span(this, AbsoluteSizeSpan(size))
fun CharSequence.setColor(color: Int) = span(this, ForegroundColorSpan(color))
fun CharSequence.setBackground(color: Int) = span(this, BackgroundColorSpan(color))
fun CharSequence.setUrl(url: String) = span(this, URLSpan(url))
fun CharSequence.setFont(font: Typeface) = span(this, CustomTypefaceSpan("",font))