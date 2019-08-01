package com.sagar.android.paymentgateway.repository

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sagar.android.logutilmaster.LogUtil
import com.sagar.android.paymentgateway.core.KeyWordsAndConstants
import com.sagar.android.paymentgateway.model.*
import com.sagar.android.paymentgateway.repository.retrofit.ApiInterface
import com.sagar.android.paymentgateway.ui.splash.Splash
import com.sagar.android.paymentgateway.util.StatusCode
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.system.exitProcess


class Repository(
    private var apiInterface: ApiInterface,
    private var preference: SharedPreferences,
    private var logUtil: LogUtil,
    private var application: Application
) {

    //vars
    var signUpResult: MutableLiveData<Event<Result>> = MutableLiveData()
    var loginResult: MutableLiveData<Event<Result>> = MutableLiveData()
    var logoutResult: MutableLiveData<Event<Result>> = MutableLiveData()
    var razorPayKeySuccess: MutableLiveData<Event<String>> = MutableLiveData()
    var orderIdSuccess: MutableLiveData<Event<JSONObject>> = MutableLiveData()
    var verifyPaymentSuccess: MutableLiveData<Event<Result>> = MutableLiveData()
    var paymentFail: MutableLiveData<Event<String>> = MutableLiveData()

    //api calls
    fun signUp(
        name: String,
        email: String,
        password: String
    ) {
        apiInterface.signup(
            SignUpRequest(
                name, email, password
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                signUpResult.postValue(
                                    Event(
                                        content = Result(result = com.sagar.android.paymentgateway.core.Result.OK)
                                    )
                                )
                                saveUserData(
                                    JSONObject(
                                        t.body()?.string()
                                    )
                                )
                                loggedIn()
                            }
                            else -> {
                                signUpResult.postValue(
                                    Event(
                                        content = Result(
                                            result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                            message = getErrorMessage(t.errorBody()!!)
                                        )
                                    )
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        signUpResult.postValue(
                            Event(
                                content = Result(
                                    result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                    message = getErrorMessage(e)
                                )
                            )
                        )
                    }
                }
            )
    }

    fun login(
        email: String,
        password: String
    ) {
        apiInterface.login(
            LoginRequest(email, password)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                loginResult.postValue(
                                    Event(
                                        content = Result(result = com.sagar.android.paymentgateway.core.Result.OK)
                                    )
                                )
                                saveUserData(
                                    JSONObject(
                                        t.body()?.string()
                                    )
                                )
                                loggedIn()
                            }
                            else -> {
                                loginResult.postValue(
                                    Event(
                                        content = Result(
                                            result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                            message = getErrorMessage(t.errorBody()!!)
                                        )
                                    )
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        loginResult.postValue(
                            Event(
                                content = Result(
                                    result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                    message = getErrorMessage(e)
                                )
                            )
                        )
                    }
                }
            )
    }

    fun logout() {
        apiInterface.logout(
            getAuthToken()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                clearAllData()
                                logoutResult.postValue(
                                    Event(
                                        content = Result(result = com.sagar.android.paymentgateway.core.Result.OK)
                                    )
                                )
                            }
                            StatusCode.Unauthorized.code -> notAuthorised()
                            else -> {
                                logoutResult.postValue(
                                    Event(
                                        content = Result(
                                            result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                            message = getErrorMessage(t.errorBody()!!)
                                        )
                                    )
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        logoutResult.postValue(
                            Event(
                                content = Result(
                                    result = com.sagar.android.paymentgateway.core.Result.FAIL,
                                    message = getErrorMessage(e)
                                )
                            )
                        )
                    }
                }
            )
    }

    fun getRazorPayKey() {
        apiInterface.getRazorPayKey(
            getAuthToken()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                val json = JSONObject(t.body()!!.string())
                                razorPayKeySuccess.postValue(
                                    Event(json.getString("key"))
                                )
                            }
                            StatusCode.Unauthorized.code -> notAuthorised()
                            else -> {
                                paymentFail.postValue(
                                    Event("Failed to get server credentials")
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        paymentFail.postValue(
                            Event(getErrorMessage(e))
                        )
                    }

                }
            )
    }

    fun createOrderId(
        req: CreateOrderIdReq
    ) {
        apiInterface.createOrderId(
            getAuthToken(),
            req
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                val json = JSONObject(t.body()!!.string())
                                orderIdSuccess.postValue(
                                    Event(json)
                                )
                            }
                            StatusCode.Unauthorized.code -> notAuthorised()
                            else -> {
                                paymentFail.postValue(
                                    Event("Failed to create order id")
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        paymentFail.postValue(
                            Event(getErrorMessage(e))
                        )
                    }

                }
            )
    }

    fun verifyPayment(
        orderId: String,
        paymentId: String,
        signature: String
    ) {
        apiInterface.verifyPayment(
            getAuthToken(),
            VerifyPaymentReq(
                orderId, paymentId, signature
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        when (t.code()) {
                            StatusCode.OK.code -> {
                                verifyPaymentSuccess.postValue(
                                    Event(Result(result = com.sagar.android.paymentgateway.core.Result.OK))
                                )
                            }
                            StatusCode.Unauthorized.code -> notAuthorised()
                            else -> {
                                paymentFail.postValue(
                                    Event(getErrorMessage(t.errorBody()!!))
                                )
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        paymentFail.postValue(
                            Event(getErrorMessage(e))
                        )
                    }

                }
            )
    }

    //methods
    fun isLoggedIn(): Boolean {
        return preference.getBoolean(
            KeyWordsAndConstants.IS_LOGGED_IN,
            false
        )
    }

    //private methods
    private fun saveUserData(jsonObject: JSONObject) {
        preference.edit()
            .putString(
                KeyWordsAndConstants.USER_DATA,
                jsonObject.toString()
            )
            .apply()

        logUtil.logV(getUserData().toString())
    }

    private fun loggedIn() {
        preference.edit().putBoolean(
            KeyWordsAndConstants.IS_LOGGED_IN,
            true
        )
            .apply()
    }

    private fun clearAllData() {
        preference.edit().clear().apply()
    }

    private fun getUserData(): UserData {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        if (
            preference.getString(
                KeyWordsAndConstants.USER_DATA, ""
            ) == ""
        )
            return UserData()
        return Gson().fromJson(
            preference.getString(
                KeyWordsAndConstants.USER_DATA,
                ""
            ),
            UserData::class.java
        )
    }

    private fun getToken(): String {
        return getUserData().token
    }

    private fun getAuthToken(): String {
        return "Bearer " + getToken()
    }

    private fun notAuthorised() {
        clearAllData()

        Thread(
            Runnable {
                Thread.sleep(1000)
                restartApp()
            }
        ).start()
    }

    private fun restartApp() {
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                application,
                123,
                Intent(application, Splash::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val alarmManager: AlarmManager = application.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent
        )

        exitProcess(0)
    }

    //util function
    private fun getErrorMessage(throwable: Throwable): String {
        return if (throwable is HttpException) {
            val responseBody = throwable.response()!!.errorBody()
            try {
                val jsonObject = JSONObject(responseBody!!.string())
                jsonObject.getString("error")
            } catch (e: Exception) {
                e.message!!
            }
        } else (when (throwable) {
            is SocketTimeoutException -> "Timeout occurred"
            is IOException -> "network error"
            else -> throwable.message
        })!!
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("error")
        } catch (e: Exception) {
            "Something went wrong."
        }
    }
}