package com.sonny.basetest.base

import android.app.Application
import android.content.Context

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        start(this@MyApplication)
    }


    companion object {
        const val TIMEOUT: Long = 30
        lateinit var context: Context
            private set
    }

}