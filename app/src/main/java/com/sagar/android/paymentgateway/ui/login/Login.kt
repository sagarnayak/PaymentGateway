package com.sagar.android.paymentgateway.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.databinding.ActivityLoginBinding
import com.sagar.android.paymentgateway.ui.signup.SignUp
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class Login : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by org.kodein.di.android.kodein()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.context = this

        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            title = ""
        }
    }

    fun onClickSignUp(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, SignUp::class.java))
        finish()
    }
}
