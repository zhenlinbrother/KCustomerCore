package com.lit.kcustomercore.ui.home.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.eyepetizer.android.logic.model.Daily
import com.lit.kcustomercore.net.MainPageRepository
import com.lit.kcustomercore.net.api.ApiService
import java.lang.Exception

class DailyViewModel (val repository: MainPageRepository) : ViewModel() {
    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    var isFirst = true

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val daily = repository.refreshDaily(it)
                Result.success(daily)
            } catch (e : Exception){
                Result.failure<Daily>(e)
            }
            emit(result)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = ApiService.DAILY_URL
        isFirst = true
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
        isFirst = false
    }
}