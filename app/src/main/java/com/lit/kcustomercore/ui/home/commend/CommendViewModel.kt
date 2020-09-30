package com.lit.kcustomercore.ui.home.commend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lit.kcustomercore.bean.HomePageRecommend
import com.lit.kcustomercore.net.MainPageRepository
import com.lit.kcustomercore.net.api.ApiService
import java.lang.Exception

class CommendViewModel(private val repository: MainPageRepository) : ViewModel(){

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    var isFirst = true

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val recommend = repository.refreshHomePageRecommend(it)
                Result.success(recommend)
            } catch (e: Exception){
                Result.failure<HomePageRecommend>(e)
            }
            emit(result)
        }
    }

    fun onRefresh(){
        requestParamLiveData.value = ApiService.HOMEPAGE_RECOMMEND_URL
        isFirst = true
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
        isFirst = false
    }
}