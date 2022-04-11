package com.lit.kcustomercore.net

import com.linc.download.jerry.JerryDownload
import com.lit.kcustomercore.bean.VideoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class VideoRepository(private val network: EyepetizerNetwork) {

    suspend fun requestVideoReplies(url: String) = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredVideoReplies = async { network.fetchVideoReplies(url) }
            val videoReplies = deferredVideoReplies.await()
            videoReplies
        }
    }

    suspend fun requestVideoDetail(videoId: Long, repliesUrl: String) = withContext(Dispatchers.IO){
        coroutineScope {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val deferredVideoBeanForClient = async { network.fetchVideoBeanForClient(videoId) }
            val downloadInfoClient = async { JerryDownload.instance?.getDownloadInfo(videoId) }
            val videoBeanForClient = deferredVideoBeanForClient.await()
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val downloadInfo = downloadInfoClient.await()
            val videoDetail = VideoDetail(videoBeanForClient, videoRelated, videoReplies, downloadInfo)

            videoDetail
        }
    }

    suspend fun requestVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String) = withContext(Dispatchers.IO){
        coroutineScope {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val downloadInfoClient = async { JerryDownload.instance?.getDownloadInfo(videoId) }
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val downloadInfo = downloadInfoClient.await()
            val videoDetail = VideoDetail(null, videoRelated, videoReplies, downloadInfo)
            videoDetail
        }
    }

    companion object {

        private var repository: VideoRepository? = null

        fun getInstance(network: EyepetizerNetwork): VideoRepository {
            if (repository == null){
                synchronized(VideoRepository::class.java){
                    if (repository == null){
                        repository = VideoRepository(network)
                    }
                }
            }

            return repository!!
        }
    }
}