package com.lit.kcustomercore.ui.newdetail

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.linc.video.helper.VideoPlayerHelper
import com.lit.base.mvvm.activity.BaseActivity
import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.Author
import com.lit.kcustomercore.bean.Consumption
import com.lit.kcustomercore.bean.Cover
import com.lit.kcustomercore.bean.WebUrl
import com.lit.kcustomercore.extension.setOnClickListener
import com.lit.kcustomercore.extension.showToast
import com.lit.kcustomercore.utils.*
import com.lit.kcustomercore.view.NoStatusFooter
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_new_detail.*
import kotlinx.coroutines.*

class NewDetailActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this, InjectorUtil.getNewDetailViewModelFactory()).get(NewDetailViewModel::class.java) }

    private lateinit var mergeAdapter: MergeAdapter
    private lateinit var relatedAdapter: NewDetailRelatedAdapter
    private lateinit var replyAdapter: NewDetailReplyAdapter
    private var orientationUtils: OrientationUtils? = null

    private val globalJob by lazy { Job() }
    private var hideTitleBarJob: Job? = null
    private var hideBottomContainerJob: Job? = null

    private var isPause = false
    private var isPlay = true

    override fun getLayoutId(): Int {
        return R.layout.activity_new_detail
    }

    override fun initView() {
        super.initView()
        initIntent()
        setupViews()
    }

    private fun setupViews() {
        relatedAdapter = NewDetailRelatedAdapter(this, viewModel.relatedDataList, viewModel.videoInfoData)
        replyAdapter = NewDetailReplyAdapter(this, viewModel.repliesDataList)
        mergeAdapter = MergeAdapter(relatedAdapter, replyAdapter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mergeAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null
        refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            this.setEnableNestedScroll(true)
            setFooterHeight(153f)
            setRefreshFooter(NoStatusFooter(this@NewDetailActivity).apply {
                setAccentColorId(R.color.white)
                setTextTitleSize(16f)
            })
            setOnRefreshListener { finish() }
            setOnLoadMoreListener{ viewModel.onLoadMore() }
        }

        setOnClickListener(
            ivPullDown, ivMore, ivShare, ivCollection, ivToWechatFriends, ivShareToWechatMemories,
            ivShareToWeibo, ivShareToQQ, ivShareToQQzone, ivAvatar, etComment, ivReply, tvReplyCount, listener = ClickListener()
        )

        observe()
        initVideoView()
        viewModel.onRefresh()
    }
    private fun observe() {
        //刷新，视频信息+相关推荐+评论
        if (!viewModel.videoDetailLiveData.hasObservers()){
            viewModel.videoDetailLiveData.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null){
                    ResponseHandler.getFailureTips(it.exceptionOrNull()).showToast()
                    return@Observer
                }
                viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null
                    || response.videoRelated.itemList.isNullOrEmpty()
                    && response.videoReplies.itemList.isNullOrEmpty()){
                    return@Observer
                }
                response.videoBeanForClient?.run {
                    viewModel.videoInfoData = VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl)
                    initVideoView()
                    relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                }
                viewModel.relatedDataList.clear()
                viewModel.repliesDataList.clear()
                viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                relatedAdapter.notifyDataSetChanged()
                replyAdapter.notifyDataSetChanged()
                when {
                    viewModel.repliesDataList.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> refreshLayout.closeHeaderOrFooter()
                }
            })
        }
        //刷新，相关推荐+评论
        if (!viewModel.repliesAndRepliesLiveData.hasObservers()){
            viewModel.repliesAndRepliesLiveData.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null){
                    ResponseHandler.getFailureTips(it.exceptionOrNull()).showToast()
                    return@Observer
                }
                viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated.itemList.isNullOrEmpty() && response.videoReplies.itemList.isNullOrEmpty()){
                    return@Observer
                }
                viewModel.relatedDataList.clear()
                viewModel.repliesDataList.clear()
                viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                relatedAdapter.notifyDataSetChanged()
                replyAdapter.notifyDataSetChanged()
                when {
                    viewModel.repliesDataList.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> refreshLayout.closeHeaderOrFooter()
                }
            })
        }
        //上拉加载，评论
        if (!viewModel.repliesLiveData.hasObservers()) {
            viewModel.repliesLiveData.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null){
                    ResponseHandler.getFailureTips(it.exceptionOrNull()).showToast()
                    return@Observer
                }
                viewModel.nextPageUrl = response.nextPageUrl
                if (response.itemList.isNullOrEmpty()){
                    return@Observer
                }
                val itemCount = replyAdapter.itemCount
                viewModel.repliesDataList.addAll(response.itemList)
                replyAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
                replyAdapter.notifyDataSetChanged()
                if (response.nextPageUrl.isNullOrEmpty())
                    refreshLayout.finishLoadMoreWithNoMoreData()
                else
                    refreshLayout.closeHeaderOrFooter()
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initIntent()
        viewModel.onRefresh()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.anl_push_bottom_out)
    }

    fun initIntent() {
        if (intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEO_INFO) == null
            && intent.getLongExtra(EXTRA_VIDEO_ID, 0L) == 0L) {
            "跳转界面出现未知异常".showToast()
            finish()
        }

        viewModel.videoInfoData = intent.getParcelableExtra(EXTRA_VIDEO_INFO)
        viewModel.videoId = intent.getLongExtra(EXTRA_VIDEO_ID, 0L)
    }

    private fun initVideoView(){
        viewModel.videoInfoData?.run {
            videoPlayer.backButton.setOnClickListener { finish() }
            //全屏辅助
            orientationUtils = OrientationUtils(this@NewDetailActivity, videoPlayer)
            //初始化不打开外部旋转
            orientationUtils!!.isEnable = false
            //初始化配置
            VideoPlayerHelper.optionPlayer(videoPlayer,
                this@NewDetailActivity,
                playUrl,
                true,
                title)

            videoPlayer.setIsTouchWiget(true)

            videoPlayer.setVideoAllCallBack(object : GSYSampleCallBack(){
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils!!.isEnable = true
                }

                override fun onStartPrepared(url: String?, vararg objects: Any?) {
                    super.onStartPrepared(url, *objects)
                    flHeader.gone()
                    llShare.gone()
                }

                override fun onClickBlank(url: String?, vararg objects: Any?) {
                    super.onClickBlank(url, *objects)
                    switchTitleBarVisible()
                }

                override fun onClickStop(url: String?, vararg objects: Any?) {
                    super.onClickStop(url, *objects)
                    delayHideBottomContainer()
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                    orientationUtils?.backToProtVideo()
                }
            })

            videoPlayer.startPlayLogic()
        }
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()

        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }

        super.onBackPressed()
    }

    override fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
        isPause = true
        super.onPause()
    }

    override fun onResume() {
        videoPlayer.currentPlayer.onVideoResume()
        isPause = false
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.gsyVideoManager.setListener(videoPlayer.gsyVideoManager.lastListener())
        videoPlayer.gsyVideoManager.setLastListener(null)
        videoPlayer.cancel()
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils != null){
            orientationUtils!!.releaseListener()
            try {
                val mOrientationEventListener = OrientationUtils::class.java.getDeclaredField("mOrientationEventListener")
                val contextField = OrientationUtils::class.java.getField("mActivity")
                contextField.isAccessible = true
                contextField.set(orientationUtils, null)
                mOrientationEventListener.isAccessible = true
                val listener = mOrientationEventListener.get(orientationUtils)
                val mSensorEventListener = OrientationUtils::class.java.getDeclaredField("mSensorEventListener")
                mSensorEventListener.isAccessible = true
                mSensorEventListener.set(listener, null)
                val mSensorManager = OrientationUtils::class.java.getDeclaredField("mSensorManager")
                mSensorManager.isAccessible = true
                mSensorManager.set(listener, null)
            } catch (e: NoSuchFieldException){
                e.printStackTrace()
            } catch (e: IllegalAccessException){
                e.printStackTrace()
            }

            orientationUtils = null
        }
        VideoPlayerHelper.release()
        globalJob.cancel()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPause && !isPause){
            videoPlayer.onConfigurationChanged(this,
                newConfig,
                orientationUtils,
                true,
                true)
        }
    }

    private fun switchTitleBarVisible() {
        if(videoPlayer.currentPlayer.currentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) return
        if (flHeader.visibility == View.VISIBLE){
            hideTitleBar()
        } else {
            flHeader.visibleAlphaAnimation(1000)
            ivPullDown.visibleAlphaAnimation(1000)
            ivCollection.visibleAlphaAnimation(1000)
            ivMore.visibleAlphaAnimation(1000)
            ivShare.visibleAlphaAnimation(1000)
            delayHideTitleBar()
        }
    }

    private fun hideTitleBar() {
        flHeader.invisibleAlphaAnimation(1000)
        ivPullDown.goneAlphaAnimation(1000)
        ivCollection.goneAlphaAnimation(1000)
        ivMore.goneAlphaAnimation(1000)
        ivShare.goneAlphaAnimation(1000)
    }

    private fun delayHideTitleBar() {
        hideTitleBarJob?.cancel()
        hideTitleBarJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    private fun delayHideBottomContainer() {
        hideBottomContainerJob?.cancel()
        hideBottomContainerJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(videoPlayer.dismissControlTime.toLong())
            videoPlayer.startButton.gone()
        }
    }

    private fun scrollTop(){
        if (relatedAdapter.itemCount != 0){
            recyclerView.scrollToPosition(0)
            refreshLayout.invisibleAlphaAnimation(2500)
            refreshLayout.visibleAlphaAnimation(1500)
        }
    }

    private fun scrollRepliesTop() {
        val targetPosition = (relatedAdapter.itemCount - 1) + 2
        if (targetPosition < mergeAdapter.itemCount - 1){
            recyclerView.smoothScrollToPosition(targetPosition)
        }
    }

    inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            viewModel.videoInfoData?.let {
                when(v){
                    ivPullDown -> finish()
                    ivReply, tvReplyCount -> scrollRepliesTop()
                }
            }
        }

    }

    @Parcelize
    data class VideoInfo (
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover,
        val author: Author?,
        val webUrl: WebUrl
    ): Parcelable

    companion object {
        const val TAG = "NewDetailActivity"

        const val EXTRA_VIDEO_INFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"

        fun start(context: Activity, videoInfo: VideoInfo){
            val starter = Intent(context, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_INFO, videoInfo)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }

        fun start(context: Activity, videoId: Long){
            val starter = Intent(context, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }
}