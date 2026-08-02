package com.sudhirtheindian.instagramclone

import android.app.Application
import com.sudhirtheindian.instagramclone.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class InstagramApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@InstagramApp)
        }
    }
}
