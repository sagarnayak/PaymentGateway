package com.sagar.android.paymentgateway.application

import android.app.Application
import com.sagar.android.logutilmaster.LogUtil
import com.sagar.android.paymentgateway.core.KeyWordsAndConstants
import com.sagar.android.paymentgateway.di.NetworkModule
import com.sagar.android.paymentgateway.repository.Repository
import com.sagar.android.paymentgateway.ui.splash.SplashViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.BuildConfig
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ApplicationClass : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {

        import(androidXModule(this@ApplicationClass))

        bind() from singleton {
            LogUtil(
                LogUtil.Builder()
                    .setCustomLogTag(KeyWordsAndConstants.LOG_TAG)
                    .setShouldHideLogInReleaseMode(false, BuildConfig.DEBUG)
            )
        }

        bind() from singleton { NetworkModule(instance()).apiInterface }

        bind<Repository>() with singleton { Repository(instance(), instance()) }

        bind() from provider { SplashViewModelProvider(instance()) }
    }
}