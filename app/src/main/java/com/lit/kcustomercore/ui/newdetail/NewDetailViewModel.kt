package com.lit.kcustomercore.ui.newdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lit.kcustomercore.bean.VideoDetail
import com.lit.kcustomercore.bean.VideoRelated
import com.lit.kcustomercore.bean.VideoReplies
import com.lit.kcustomercore.net.VideoRepository
import com.lit.kcustomercore.net.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewDetailViewModel(repository: VideoRepository) : ViewModel(){

    var relatedDataList = ArrayList<VideoRelated.Item>()

    var repliesDataList = ArrayList<VideoReplies.Item>()

    var videoInfoData: NewDetailActivity.VideoInfo? = null

    var videoId: Long = 0L
    var isFirst = true

    private var repliesLiveData_ = MutableLiveData<String>()
    private var videoDetailLiveData_ = MutableLiveData<RequestParam>()
    private var repliesAndRepliesLiveData_ = MutableLiveData<RequestParam>()

    var nextPageUrl: String? = null

    val videoDetailLiveData = Transformations.switchMap(videoDetailLiveData_){
        liveData {
            val result = try {
                val videoDetail = repository.requestVideoDetail(it.videoId, it.repliesUrl)
                Result.success(videoDetail)
            } catch (e : Exception){
                Result.failure<VideoDetail>(e)
            }
            emit(result)
        }
    }

    val repliesAndRepliesLiveData = Transformations.switchMap(repliesAndRepliesLiveData_) {
        liveData {
            val result = try {
                val videoDetail = repository.requestVideoRelatedAndVideoReplies(it.videoId, it.repliesUrl)
                //setDataType(videoDetail)
                Result.success(videoDetail)
            } catch (e : Exception){
                Result.failure<VideoDetail>(e)
            }
            emit(result)
        }
    }

    val repliesLiveData = Transformations.switchMap(repliesLiveData_) {
        liveData {
            val result = try {
                val videoDetail = repository.requestVideoReplies(it)
                Result.success(videoDetail)
            } catch (e : Exception){
                Result.failure<VideoReplies>(e)
            }
            emit(result)
        }
    }

    suspend fun setDataType(videoDetail: VideoDetail) = withContext(Dispatchers.IO){
        if (videoDetail.videoBeanForClient != null){

        }
    }

    fun onRefresh() {
        if (videoInfoData == null){
            videoDetailLiveData_.value = RequestParam(videoId, "${ApiService.VIDEO_REPLIES_URL}$videoId")
        } else {
            repliesAndRepliesLiveData_.value = RequestParam(videoInfoData?.videoId ?: 0L, "${ApiService.VIDEO_REPLIES_URL}${videoInfoData?.videoId ?: 0L}")
        }
        isFirst = false
    }

    fun onLoadMore(){
        repliesLiveData_.value = nextPageUrl ?: ""
        isFirst = true
    }

    inner class RequestParam(val videoId: Long, val repliesUrl: String)
}