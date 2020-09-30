package com.lit.kcustomercore.ui.home.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lit.kcustomercore.bean.Discovery
import com.lit.kcustomercore.net.MainPageRepository
import com.lit.kcustomercore.net.api.ApiService
import java.lang.Exception

class DiscoveryViewModel(repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<Discovery.Item>()

    private var requestParamLiveData = MutableLiveData<String>()
    var isFirst = true

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val discovery = repository.requestDiscovery(it)
                Result.success(discovery)
            } catch (e: Exception){
                Result.failure<Discovery>(e)
            }
            emit(result)
        }
    }

    fun onRefresh(){
        requestParamLiveData.value = ApiService.DISCOVERY_URL
        isFirst = true
    }

    fun onLoadMore(){
        requestParamLiveData.value = nextPageUrl ?: ""
        isFirst = false
    }
}