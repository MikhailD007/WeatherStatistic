package org.vimteam.weatherstatistic.data.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.joda.time.LocalDate
import java.lang.reflect.Type

class LocalDateConverter : JsonDeserializer<LocalDate> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return json?.let { LocalDate(it.asString.split("T")[0]) } ?: LocalDate.now()
    }
}