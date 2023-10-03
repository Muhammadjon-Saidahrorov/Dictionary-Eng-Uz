package uz.gita.dictionarymn.utils

import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.gita.dictionarymn.MainActivity
import uz.gita.dictionarymn.R
import uz.gita.dictionarymn.app.App

fun String.spannable(query: String): SpannableString {
    val span = SpannableString(this)
    val color = ForegroundColorSpan(ContextCompat.getColor(App.context, R.color.purple_500))
    var startIndex = this.indexOf(query, 0, true)
    if (startIndex > -1) {
        span.setSpan(
            color,
            startIndex,
            startIndex + query.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return span
}

fun FragmentActivity.myIntentActivity() {
    startActivity(Intent(this, MainActivity::class.java))
}

fun Fragment.myIntent() {
    requireActivity().myIntentActivity()
}