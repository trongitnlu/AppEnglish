package com.example.nvtrong.appenglish

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        var realmConfiguration = RealmConfiguration.Builder().name("myrealm.realm").build()
        realm = Realm.getInstance(realmConfiguration)
    }
}
