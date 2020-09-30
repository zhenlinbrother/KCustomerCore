package com.lit.kcustomercore.net

import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.lit.kcustomercore.extension.screenPixel
import com.lit.kcustomercore.utils.GlobalUtil
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 网络服务基础设置
 * version      : 1.0.0
 */
object ServiceCreator {
    const val BASE_URL = "http://baobab.kaiyanapp.com/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(LogInterceptor())
        .addInterceptor(BasicParamsInterceptor())
        .build()

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder()
                .registerTypeAdapterFactory(GsonTypeAdapterFactory()).create()))

    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 头部拦截器
     */
    class HeaderInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent",System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }
    }


    /**
     * 日志打印拦截类
     * @property random Random
     */
     class LogInterceptor : Interceptor {
        private val random = Random()

        @Throws(IOException::class)
        override fun intercept(@NonNull chain: Interceptor.Chain): Response {
            val requestId = random.nextInt(1000000)
            val request: Request = chain.request()
            buildRequestLog(requestId, request)
            val reqTime = System.currentTimeMillis()
            val response = chain.proceed(request)
            val url: String = request.url.toUri().toString()
            buildResponseLog(requestId, url, reqTime, response)
            return response
        }

        private fun buildRequestLog(requestId: Int, request: Request) {
            if (!isShow) {
                return
            }
            val stringBuilder = StringBuilder()
            stringBuilder.append("HTTP Log").append(CRLF)
            stringBuilder
                .append(TOP_LEFT_LINE)
                .append("------------request------------------------------------------")
                .append(CRLF)
            appendKeyAndValue(stringBuilder, "id", requestId.toString() + "")
            appendKeyAndValue(stringBuilder, "url", request.url.toUri().toString())
            appendKeyAndValue(stringBuilder, "method", request.method)
            stringBuilder.append(VERTICAL_LINE).append(CRLF)
            stringBuilder.append(VERTICAL_LINE).append(" ").append("head params")
                .append(CRLF)
            val headerMap: Map<String, List<String>> =
                request.headers.toMultimap()
            for ((key, value1) in headerMap) {
                for (value in value1) {
                    stringBuilder
                        .append(VERTICAL_LINE)
                        .append(" ")
                        .append(MIDDLE_LINE)
                        .append(key)
                        .append(COLON)
                        .append(value)
                        .append(CRLF)
                }
            }
            stringBuilder.append(VERTICAL_LINE).append(CRLF)
            stringBuilder
                .append(VERTICAL_LINE)
                .append(" ")
                .append(if (request.body == null) "It's empty body" else bodyToString(request.body))
                .append(CRLF)
            stringBuilder
                .append(BOTTOM_LEFT_LINE)
                .append("-----------------------------------------------------------")
            Log.i("LogInterceptor", stringBuilder.toString())
        }

        private fun buildResponseLog(
            requestId: Int,
            url: String,
            reqTime: Long,
            response: Response
        ) {
            if (!isShow) {
                return
            }
            val stringBuilder = StringBuilder()
            stringBuilder.append("HTTP Log").append(CRLF)
            stringBuilder
                .append(TOP_LEFT_LINE)
                .append("------------response------------------------------------------")
                .append(CRLF)
            appendKeyAndValue(stringBuilder, "id", requestId.toString() + "")
            appendKeyAndValue(stringBuilder, "time",  "${System.currentTimeMillis() - reqTime}")
            appendKeyAndValue(stringBuilder, "url", url)
            appendKeyAndValue(stringBuilder, "message", response.message)
            appendKeyAndValue(stringBuilder, "code", response.code.toString() + "")
            stringBuilder.append(VERTICAL_LINE).append(CRLF)
            stringBuilder.append(VERTICAL_LINE).append(" ").append("head params")
                .append(CRLF)
            val headerMap =
                response.headers.toMultimap()
            for ((key, value1) in headerMap) {
                for (value in value1) {
                    stringBuilder
                        .append(VERTICAL_LINE)
                        .append(" ")
                        .append(MIDDLE_LINE)
                        .append(key)
                        .append(COLON)
                        .append(value)
                        .append(CRLF)
                }
            }
            stringBuilder.append(VERTICAL_LINE).append(CRLF)
            if (url.contains("api/file/originals")) {
                stringBuilder
                    .append(VERTICAL_LINE)
                    .append(" ")
                    .append("It's the download request.")
                    .append(CRLF)
            } else {
                try {
                    val body: String
                    body = if (response.body == null || response.body!!.contentLength() == 0L) {
                        "It's empty body"
                    } else {
                        val source = response.body!!.source()
                        source.request(Long.MAX_VALUE)
                        source.buffer().clone().readString(UTF8)
                    }
                    stringBuilder
                        .append(VERTICAL_LINE)
                        .append(" ")
                        .append(body)
                        .append(CRLF)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            stringBuilder
                .append(BOTTOM_LEFT_LINE)
                .append("-----------------------------------------------------------")
            Log.i("LogInterceptor", stringBuilder.toString())
        }

        private fun appendKeyAndValue(
            stringBuilder: StringBuilder,
            key: String,
            value: String
        ) {
            stringBuilder
                .append(VERTICAL_LINE)
                .append(" ")
                .append(key)
                .append(COLON)
                .append(value)
                .append(CRLF)
        }

        private fun bodyToString(request: RequestBody?): String {
            return try {
                val buffer = Buffer()
                if (request == null) {
                    return ""
                }
                request.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }

        companion object {
            private const val COLON = ": "
            private const val TOP_LEFT_LINE = "┌"
            private const val MIDDLE_LINE = "├"
            private const val BOTTOM_LEFT_LINE = "└"
            private const val CRLF = "\r\n"
            private const val VERTICAL_LINE = "│"
            private const val HORIZONTAL_LINE = "─"
            private const val isShow = true
            private val UTF8: Charset = Charset.forName("UTF-8")
        }
    }

    class BasicParamsInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("udid", GlobalUtil.getDeviceSerial())
                addQueryParameter("vc", GlobalUtil.eyepetizerVersionCode.toString())
                addQueryParameter("vn", GlobalUtil.eyepetizerVersionName)
                addQueryParameter("size", screenPixel())
                addQueryParameter("deviceModel", GlobalUtil.deviceModel)
                addQueryParameter("first_channel", GlobalUtil.deviceBrand)
                addQueryParameter("last_channel", GlobalUtil.deviceBrand)
                addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
            }.build()
            val request = originalRequest.newBuilder().url(url).method(originalRequest.method,
                originalRequest.body
            ).build()
            return chain.proceed(request)
        }
    }
}