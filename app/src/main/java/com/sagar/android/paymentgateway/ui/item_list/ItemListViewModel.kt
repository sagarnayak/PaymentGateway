package com.sagar.android.paymentgateway.ui.item_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.repository.Repository

class ItemListViewModel(private var repository: Repository) : ViewModel() {

    var logoutResult: MediatorLiveData<Event<Result>> = MediatorLiveData()

    init {
        bindToRepo()
    }

    private fun bindToRepo() {
        logoutResult.addSource(
            repository.logoutResult
        ) { t -> logoutResult.postValue(t) }
    }

    fun logout() {
        repository.logout()
    }
}