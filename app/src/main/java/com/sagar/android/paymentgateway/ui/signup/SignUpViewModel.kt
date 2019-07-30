package com.sagar.android.paymentgateway.ui.signup

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.repository.Repository

class SignUpViewModel(var repository: Repository) : ViewModel() {

    var signUpResult: MediatorLiveData<Event<Result>> = MediatorLiveData()

    init {
        bindToRepo()
    }

    fun bindToRepo(): Unit {
        signUpResult.addSource(
            repository.signUpResult
        ) { t -> signUpResult.postValue(t) }
    }

    fun signUp(
        name: String,
        email: String,
        password: String
    ): Unit {
        repository.signUp(
            name, email, password
        )
    }
}