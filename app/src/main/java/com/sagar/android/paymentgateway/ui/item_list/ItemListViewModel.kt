package com.sagar.android.paymentgateway.ui.item_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagar.android.paymentgateway.model.CreateOrderIdReq
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.repository.Repository
import org.json.JSONObject

class ItemListViewModel(private var repository: Repository) : ViewModel() {

    var logoutResult: MediatorLiveData<Event<Result>> = MediatorLiveData()
    var razorPayKeySuccess: MediatorLiveData<Event<String>> = MediatorLiveData()
    var orderIdSuccess: MediatorLiveData<Event<JSONObject>> = MediatorLiveData()
    var verifyPaymentSuccess: MediatorLiveData<Event<Result>> = MediatorLiveData()
    var paymentFail: MediatorLiveData<Event<String>> = MediatorLiveData()

    init {
        bindToRepo()
    }

    private fun bindToRepo() {
        logoutResult.addSource(
            repository.logoutResult
        ) { t -> logoutResult.postValue(t) }

        razorPayKeySuccess.addSource(
            repository.razorPayKeySuccess
        ) { t -> razorPayKeySuccess.postValue(t) }

        orderIdSuccess.addSource(
            repository.orderIdSuccess
        ) { t -> orderIdSuccess.postValue(t) }

        verifyPaymentSuccess.addSource(
            repository.verifyPaymentSuccess
        ) { t -> verifyPaymentSuccess.postValue(t) }

        paymentFail.addSource(
            repository.paymentFail
        ) { t -> paymentFail.postValue(t) }
    }

    fun logout() {
        repository.logout()
    }

    fun getRazorPayKey() {
        repository.getRazorPayKey()
    }

    fun createOrderId(req: CreateOrderIdReq) {
        repository.createOrderId(req)
    }

    fun verifyPayment(
        orderId: String,
        paymentId: String,
        signature: String
    ) {
        repository.verifyPayment(
            orderId, paymentId, signature
        )
    }
}