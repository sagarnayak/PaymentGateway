package com.sagar.android.paymentgateway.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.ui.item_list.ItemsList
import com.sagar.android.paymentgateway.ui.signup.SignUp
import com.sagar.android.paymentgateway.util.SuperActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class Login : SuperActivity(), KodeinAware {

    override val kodein: Kodein by org.kodein.di.android.kodein()

    private lateinit var binding: com.sagar.android.paymentgateway.databinding.ActivityLoginBinding
    private val viewModelProvider: LoginViewModelProvider by instance()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.context = this

        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            title = ""
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider).get(LoginViewModel::class.java)

        bindToViewModel()
    }

    fun onClickLogin(@Suppress("UNUSED_PARAMETER") view: View) {
        if (binding.contentLogin.editTextEmail.text.isEmpty()) {
            showMessageInDialog("Please enter email id")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.contentLogin.editTextEmail.text.toString()).matches()) {
            showMessageInDialog("Please enter a valid email")
            return
        }
        if (binding.contentLogin.editTextPassword.text.isEmpty()) {
            showMessageInDialog("Please enter password")
            return
        }

        showProgress()

        viewModel.login(
            binding.contentLogin.editTextEmail.text.toString().trim(),
            binding.contentLogin.editTextPassword.text.toString().trim()
        )
    }

    fun onClickSignUp(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, SignUp::class.java))
        finish()
    }

    private fun bindToViewModel() {
        viewModel.loginResult
            .observe(
                this,
                Observer<Event<Result>> { t ->
                    if (t!!.shouldReadContent())
                        processLoginResult(t.getContent()!!)
                }
            )
    }

    private fun processLoginResult(result: Result) {
        hideProgress()
        if (result.result == com.sagar.android.paymentgateway.core.Result.FAIL) {
            showMessageInDialog(result.message)
            return
        }

        startActivity(Intent(this, ItemsList::class.java))
        finish()
    }
}
