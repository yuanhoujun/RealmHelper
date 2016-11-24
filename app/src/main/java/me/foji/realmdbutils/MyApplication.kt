package me.foji.realmdbutils

import android.app.Application
import android.os.Environment
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File

/**
 * @author Scott Smith  @Date 2016年11月2016/11/23日 14:20
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().name("realm_helper").directory(directory()).schemaVersion(1).build())
    }

    private fun directory(): File {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return File(Environment.getExternalStorageDirectory().absolutePath + "/realm")
        }
        return cacheDir
    }
}