package com.sagar.android.paymentgateway.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sagar.android.paymentgateway.R
import com.sagar.android.paymentgateway.databinding.ActivitySignUpBinding
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.ui.item_list.ItemsList
import com.sagar.android.paymentgateway.ui.login.Login
import com.sagar.android.paymentgateway.util.SuperActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SignUp : SuperActivity(), KodeinAware {

    override val kodein: Kodein by org.kodein.di.android.kodein()

    private val viewModelProvider: SignUpViewModelProvider by instance()
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.context = this

        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProviders.of(this, viewModelProvider)
            .get(SignUpViewModel::class.java)

        bindToViewModel()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindToViewModel() {
        viewModel.signUpResult.observe(
            this,
            Observer<Event<Result>> { t ->
                if (t!!.shouldReadContent())
                    processSignUpResult(t.getContent()!!)
            }
        )
    }

    fun onClickSignUp(@Suppress("UNUSED_PARAMETER") view: View) {
        if (binding.contentSignUp.editTextUserName.text.isEmpty()) {
            showMessageInDialog(
                "Please provide a userName"
            )
            return
        }
        if (binding.contentSignUp.editTextEmail.text.isEmpty()) {
            showMessageInDialog(
                "Please provide a email"
            )
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.contentSignUp.editTextEmail.text.trim()).matches()) {
            showMessageInDialog(
                "Please provide a valid email"
            )
            return
        }
        if (binding.contentSignUp.editTextPassword.text.isEmpty()) {
            showMessageInDialog(
                "Please provide a password"
            )
            return
        }

        viewModel.signUp(
            binding.contentSignUp.editTextUserName.text.toString().trim(),
            binding.contentSignUp.editTextEmail.text.toString().trim(),
            binding.contentSignUp.editTextPassword.text.toString().trim()
        )

        showProgress()
    }

    private fun processSignUpResult(result: Result) {
        hideProgress()

        if (result.result == com.sagar.android.paymentgateway.core.Result.FAIL) {
            showMessageInDialog(result.message)
            return
        }

        startActivity(Intent(this, ItemsList::class.java))
        finish()
    }
}
