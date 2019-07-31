package com.sagar.android.paymentgateway.ui.item_list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.sagar.android.logutilmaster.LogUtil
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.databinding.ActivityItemsListBinding
import com.sagar.android.paymentgateway.model.CreateORderIdReq
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.NotesForOrderIdReq
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.ui.item_list.adapter.ItemAdapter
import com.sagar.android.paymentgateway.ui.login.Login
import com.sagar.android.paymentgateway.util.SuperActivity
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class ItemsList : SuperActivity(), KodeinAware, PaymentResultWithDataListener {

    override val kodein by org.kodein.di.android.kodein()

    private lateinit var binding: ActivityItemsListBinding
    private val viewModelProvider: ItemListViewModelProvider by instance()
    private val logUtil: LogUtil by instance()
    private lateinit var viewModel: ItemListViewModel
    private val checkout = Checkout()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_items_list)
        binding.context = this

        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            title = ""
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider).get(ItemListViewModel::class.java)

        bindToViewModel()

        setList()

        Checkout.preload(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.item_list_menu,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_logout) {
            processForLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindToViewModel() {
        viewModel.logoutResult.observe(
            this,
            Observer<Event<Result>> { t ->
                if (t!!.shouldReadContent())
                    processLogoutResult(t.getContent()!!)
            }
        )

        viewModel.razorPayKeySuccess.observe(
            this,
            Observer<Event<String>> { t ->
                if (t!!.shouldReadContent())
                    gotRazorPayKey(t.getContent()!!)
            }
        )

        viewModel.orderIdSuccess.observe(
            this,
            Observer<Event<JSONObject>> { t ->
                if (t!!.shouldReadContent())
                    orderIdCreated(t.getContent()!!)
            }
        )

        viewModel.verifyPaymentSuccess.observe(
            this,
            Observer<Event<Result>> { t ->
                if (t!!.shouldReadContent()) {
                    t.readContent()
                    paymentVerified()
                }
            }
        )

        viewModel.paymentFail.observe(
            this,
            Observer<Event<String>> { t ->
                if (t!!.shouldReadContent())
                    paymentFailed(t.getContent()!!)
            }
        )
    }

    private fun processForLogout() {
        showProgress()
        viewModel.logout()
    }

    private fun processLogoutResult(result: Result) {
        hideProgress()
        if (result.result == com.sagar.android.paymentgateway.core.Result.FAIL) {
            showMessageInDialog(result.message)
            return
        }
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun setList() {
        binding.contentItemsList.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.contentItemsList.recyclerView.adapter = ItemAdapter(
            object : ItemAdapter.CallBack {
                override fun buy() {
                    buyItem()
                }
            }
        )
    }

    private fun buyItem() {
        showProgress()
        viewModel.getRazorPayKey()
    }

    private fun gotRazorPayKey(key: String) {
        createOrder(key)
    }

    private fun createOrder(key: String) {
        checkout.setKeyID(key)

        try {
            val createOrderIdReq = CreateORderIdReq(
                "INR",
                "50345",
                NotesForOrderIdReq(
                    "men",
                    "clothing"
                )
            )

            viewModel.createOrderId(createOrderIdReq)
        } catch (e: Exception) {
            hideProgress()
            showMessageInDialog("Payment Failed, please contact support")
        }
    }

    private fun orderIdCreated(options: JSONObject) {
        hideProgress()
        checkout.open(
            this,
            options
        )
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        showMessageInDialog(p1 ?: "Payment error")
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        logUtil.logV(p1?.paymentId ?: "payment id")
        logUtil.logV(p1?.signature ?: "signature id")
        logUtil.logV(p1?.orderId ?: "order id")
        showProgress()
        viewModel.verifyPayment(
            p1!!.orderId,
            p1.paymentId,
            p1.signature
        )
    }

    private fun paymentVerified() {
        hideProgress()
        showMessageInDialog("Payment successful")
    }

    private fun paymentFailed(message: String) {
        hideProgress()
        showMessageInDialog(message)
    }
}
