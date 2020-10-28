package com.lit.kcustomercore.ui.home.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lit.kcustomercore.net.MainPageRepository

@Suppress("UNCHECKED_CAST")
class DailyViewModelFactory(private val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DailyViewModel(repository) as T
    }
}