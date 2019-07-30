package com.sagar.android.paymentgateway.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.ui.item_list.ItemsList
import com.sagar.android.paymentgateway.ui.login.Login
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class Splash : AppCompatActivity(), KodeinAware {

    override val kodein by org.kodein.di.android.kodein()

    private val viewModelProvider: SplashViewModelProvider by instance()
    private lateinit var viewModel: SplashViewModel
    private lateinit var binding: com.sagar.android.paymentgateway.databinding.ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash
        )
        binding.context = this

        viewModel = ViewModelProviders.of(this, viewModelProvider).get(SplashViewModel::class.java)

        waitAndProceed()
    }

    private fun waitAndProceed() {
        Handler().postDelayed(
            {
                goToAppropriateActivity()
            },
            2000
        )
    }

    private fun goToAppropriateActivity() {
        if (viewModel.isLoggedIn())
            startActivity(Intent(this, ItemsList::class.java))
        else
            startActivity(Intent(this, Login::class.java))
        finish()
    }
}
