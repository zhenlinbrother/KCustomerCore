package com.lit.kcustomercore.ui.newdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lit.kcustomercore.net.VideoRepository

@Suppress("UNCHECKED_CAST")
class NewDetailViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewDetailViewModel(repository) as T
    }
}