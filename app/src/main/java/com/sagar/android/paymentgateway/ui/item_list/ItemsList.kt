package com.sagar.android.paymentgateway.ui.item_list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.databinding.ActivityItemsListBinding
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.ui.item_list.adapter.ItemAdapter
import com.sagar.android.paymentgateway.ui.login.Login
import com.sagar.android.paymentgateway.util.SuperActivity
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ItemsList : SuperActivity(), KodeinAware {

    override val kodein by org.kodein.di.android.kodein()

    private lateinit var binding: ActivityItemsListBinding
    private val viewModelProvider: ItemListViewModelProvider by instance()
    private lateinit var viewModel: ItemListViewModel

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

    private fun buyItem() {}
}
