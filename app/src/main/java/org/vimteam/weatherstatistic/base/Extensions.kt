package org.vimteam.weatherstatistic.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.LocalDate
import java.sql.Date

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun LocalDate.toSQLDate(): Date = java.sql.Date(this.toDateTimeAtStartOfDay().millis)
fun Date.toLocalDate(): LocalDate = LocalDate(this)
fun Date.formatShort(): String = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT).format(this)
inline fun <reified T> Gson.parseFromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)