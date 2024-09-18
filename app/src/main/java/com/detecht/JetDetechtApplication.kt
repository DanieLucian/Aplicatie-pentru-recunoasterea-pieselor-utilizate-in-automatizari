package com.detecht

import android.app.Application

class JetDetechtApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
    
    
}