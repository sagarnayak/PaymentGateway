package com.sagar.android.paymentgateway.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sagar.android.logutilmaster.LogUtil
import com.sagar.android.paymentgateway.core.KeyWordsAndConstants
import com.sagar.android.paymentgateway.model.Event
import com.sagar.android.paymentgateway.model.Result
import com.sagar.android.paymentgateway.model.SignUpRequest
import com.sagar.android.paymentgateway.model.UserData
import com.sagar.android.paymentgateway.repository.retrofit.ApiInterface
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


class Repository(
    var apiInterface: ApiInterface,
    var preference: SharedPreferences,
    var logUtil: LogUtil
) {

    //vars
    var signUpResult: MutableLiveData<Event<Result>> = MutableLiveData()
    var logoutResult: MutableLiveData<Event<Result>> = MutableLiveData()

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
                        if (t.code() == 200) {
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
                        } else {
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
                        if (t.code() == 200) {
                            clearAllData()
                            logoutResult.postValue(
                                Event(
                                    content = Result(result = com.sagar.android.paymentgateway.core.Result.OK)
                                )
                            )
                        } else {
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