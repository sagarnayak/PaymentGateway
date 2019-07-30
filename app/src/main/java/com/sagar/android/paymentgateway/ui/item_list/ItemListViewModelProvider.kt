package com.sagar.android.paymentgateway.ui.item_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sagar.android.paymentgateway.repository.Repository

class ItemListViewModelProvider(var repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ItemListViewModel(repository) as T
    }
}