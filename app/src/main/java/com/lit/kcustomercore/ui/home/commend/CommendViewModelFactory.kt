package com.lit.kcustomercore.ui.home.commend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lit.kcustomercore.net.MainPageRepository

@Suppress("UNCHECKED_CAST")
class CommendViewModelFactory(private val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}