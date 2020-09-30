package com.lit.kcustomercore.utils

import com.lit.kcustomercore.net.EyepetizerNetwork
import com.lit.kcustomercore.net.MainPageRepository
import com.lit.kcustomercore.ui.home.discovery.DiscoveryViewModelFactory

object InjectorUtil{

    private fun getMainPageRepository() = MainPageRepository.getInstance(EyepetizerNetwork.getInstance())

    fun getDiscoveryViewModelFactory() = DiscoveryViewModelFactory(getMainPageRepository())
}