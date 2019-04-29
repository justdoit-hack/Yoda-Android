package com.justdoit.yoda.adapter

import com.justdoit.yoda.entity.SourceTypeEnum
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class CustomJsonAdapter: JsonAdapter<SourceTypeEnum>() {

    companion object {
        val Factory = object : Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (type === SourceTypeEnum::class.java) {
                    return CustomJsonAdapter()
                }
                return null
            }
        }
    }

    override fun fromJson(reader: JsonReader): SourceTypeEnum? {
        return when(reader.nextInt()) {
            0 -> SourceTypeEnum.API
            1 -> SourceTypeEnum.ASTERISK
            else -> SourceTypeEnum.ANONYMOUS
        }
    }

    override fun toJson(writer: JsonWriter, value: SourceTypeEnum?) {
        writer.value(value.toString())
    }

}
