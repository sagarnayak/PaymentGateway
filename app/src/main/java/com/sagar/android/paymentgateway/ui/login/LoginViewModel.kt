package com.sagar.android.paymentgateway.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.repository.Repository

class LoginViewModel(val repository: Repository) : ViewModel() {

    var loginResult: MediatorLiveData<Event<Result>> = MediatorLiveData()

    init {
        bindToRepo()
    }

    private fun bindToRepo() {
        loginResult.addSource(
            repository.loginResult
        ) { t -> loginResult.postValue(t) }
    }

    fun login(email: String, password: String) {
        repository.login(email, password)
    }
}