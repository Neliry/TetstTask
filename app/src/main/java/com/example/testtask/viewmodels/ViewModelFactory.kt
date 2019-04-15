package com.example.testtask.activities

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.viewmodels.PlayListViewModel


class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(PlayListViewModel::class.java) -> {
            PlayListViewModel(application) as T
        }
        else -> throw IllegalArgumentException()
    }
}