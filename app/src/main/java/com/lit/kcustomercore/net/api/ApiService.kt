package com.lit.kcustomercore.net.api

import com.eyepetizer.android.logic.model.Daily
import com.lit.kcustomercore.bean.*
import com.lit.kcustomercore.net.ServiceCreator
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * <api> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/16
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface ApiService {

    /**
     * 首页-发现列表
     */
    @GET
    fun getDiscovery(@Url url: String): Call<Discovery>

    /**
     * 首页-推荐列表
     */
    @GET
    fun getHomePageRecommend(@Url url: String): Call<HomePageRecommend>

    /**
     * 首页-日报列表
     */
    @GET
    fun getDaily(@Url url: String): Call<Daily>

    /**
     * 视频详情-视频信息
     */
    @GET("api/v2/video/{id}")
    fun getVideoBeanForClient(@Path("id") videoId: Long): Call<VideoBeanForClient>

    /**
     * 视频详情-推荐列表
     */
    @GET("api/v4/video/related")
    fun getVideoRelated(@Query("id") videoId: Long): Call<VideoRelated>

    /**
     * 视频详情-评论列表
     */
    @GET
    fun getVideoReplies(@Url url: String): Call<VideoReplies>

    companion object {
        /**
         * 首页-发现列表
         */
        const val DISCOVERY_URL = "${ServiceCreator.BASE_URL}api/v7/index/tab/discovery"

        /**
         * 首页-推荐列表
         */
        const val HOMEPAGE_RECOMMEND_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/allRec?page=0"

        /**
         * 首页-日报列表
         */
        const val DAILY_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/feed"

        /**
         * 视频详情-评论列表URL
         */
        const val VIDEO_REPLIES_URL = "${ServiceCreator.BASE_URL}api/v2/replies/video?videoId="
    }
}