package com.lit.kcustomercore.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 主页页面对应的仓库数据管理
 * version      : 1.0.0
 */
class MainPageRepository private constructor(private val eyepetizerNetwork: EyepetizerNetwork){

    /**
     * 获取发现页面数据
     * @param url String
     * @return Discovery
     */
    suspend fun requestDiscovery(url: String) = withContext(Dispatchers.IO){
        val response = eyepetizerNetwork.fetchDiscovery(url)
        response
    }


    /**
     * 获取推荐页面数据
     * @param url String
     * @return HomePageRecommend
     */
    suspend fun refreshHomePageRecommend(url: String) = withContext(Dispatchers.IO){
        val response = eyepetizerNetwork.fetchHomePageRecommend(url)
        response
    }

    /**
     * 获取日报页面数据
     * @param url String
     * @return Daily
     */
    suspend fun refreshDaily(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchDaily(url)
        response
    }

    companion object {

        private var repository: MainPageRepository? = null

        fun getInstance(netWork: EyepetizerNetwork): MainPageRepository{
            if (repository == null){
                synchronized(MainPageRepository::class.java){
                    if (repository == null){
                        repository = MainPageRepository(netWork)
                    }
                }
            }

            return repository!!
        }
    }
}