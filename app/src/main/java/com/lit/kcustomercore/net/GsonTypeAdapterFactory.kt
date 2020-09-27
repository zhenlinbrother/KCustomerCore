package com.lit.kcustomercore.net

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 处理gson解析时类型不匹配或者空值问题
 * version      : 1.0.0
 */
class GsonTypeAdapterFactory : TypeAdapterFactory{
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
        val adapter = gson?.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter?, value: T) {
                adapter?.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader): T? {
                //gson库会通过JsonReader对json对象的每个字段进行读取，当发现类型不匹配会抛出异常
                return try {
                    adapter?.read(`in`)
                } catch (e: Throwable){
                    //我们在它抛出异常时候进行处理，让它不中断接着往下读取其他字段
                    consumeAll(`in`)
                    null
                }
            }

            private fun consumeAll(jr: JsonReader){
                if (jr.hasNext()){
                    val peek: JsonToken = jr.peek()
                    when{
                        peek === JsonToken.STRING ->{
                            jr.nextString()
                        }
                        peek === JsonToken.BEGIN_ARRAY -> {
                            jr.beginArray()
                            consumeAll(jr)
                            jr.endArray()
                        }
                        peek === JsonToken.BEGIN_OBJECT -> {
                            jr.beginObject()
                            consumeAll(jr)
                            jr.endObject()
                        }
                        peek === JsonToken.END_ARRAY -> {
                            jr.endArray()
                        }
                        peek === JsonToken.END_OBJECT -> {
                            jr.endObject()
                        }
                        peek === JsonToken.NUMBER -> {
                            jr.nextString()
                        }
                        peek === JsonToken.BOOLEAN -> {
                            jr.nextBoolean()
                        }
                        peek === JsonToken.NAME -> {
                            jr.nextName()
                            consumeAll(jr)
                        }
                        peek === JsonToken.NULL -> {
                            jr.nextNull()
                        }
                    }
                }
            }

        }
    }
}