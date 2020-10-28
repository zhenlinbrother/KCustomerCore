package com.lit.kcustomercore.utils

import com.lit.kcustomercore.net.EyepetizerNetwork
import com.lit.kcustomercore.net.MainPageRepository
import com.lit.kcustomercore.net.VideoRepository
import com.lit.kcustomercore.ui.home.commend.CommendViewModelFactory
import com.lit.kcustomercore.ui.home.daily.DailyViewModelFactory
import com.lit.kcustomercore.ui.home.discovery.DiscoveryViewModelFactory
import com.lit.kcustomercore.ui.newdetail.NewDetailViewModelFactory

object InjectorUtil{

    private fun getMainPageRepository() = MainPageRepository.getInstance(EyepetizerNetwork.getInstance())

    private fun getVideoRepository() = VideoRepository.getInstance(EyepetizerNetwork.getInstance())

    fun getDiscoveryViewModelFactory() = DiscoveryViewModelFactory(getMainPageRepository())

    fun getHomePageCommendViewModelFactory() = CommendViewModelFactory(getMainPageRepository())

    fun getNewDetailViewModelFactory() = NewDetailViewModelFactory(getVideoRepository())

    fun getDailyViewModelFactory() = DailyViewModelFactory(getMainPageRepository())
}