package com.lit.kcustomercore.net


import com.lit.kcustomercore.net.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 管理所有网络请求
 * version      : 1.0.0
 */
class EyepetizerNetwork{

    private val apiService = ServiceCreator.create(ApiService::class.java)

    suspend fun fetchDiscovery(url: String) = apiService.getDiscovery(url).await()

    suspend fun fetchHomePageRecommend(url: String) = apiService.getHomePageRecommend(url).await()

    suspend fun fetchVideoReplies(url: String) = apiService.getVideoReplies(url).await()

    suspend fun fetchVideoBeanForClient(videoId: Long) = apiService.getVideoBeanForClient(videoId).await()

    suspend fun fetchVideoRelated(videoId: Long) = apiService.getVideoRelated(videoId).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) it.resume(body)
                    else it.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {
        private var network: EyepetizerNetwork? = null

        fun getInstance(): EyepetizerNetwork{
            if (network == null){
                synchronized(EyepetizerNetwork::class.java){
                    if (network == null){
                        network = EyepetizerNetwork()
                    }
                }
            }

            return network!!
        }
    }
}