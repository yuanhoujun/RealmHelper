package me.foji.realmhelper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import io.realm.Realm
import me.foji.lifecyclebinder.LifeCycleBinder
import me.foji.lifecyclebinder.OnLifeCycleChangedListener

/**
 * Realm helper
 *
 * @author Scott Smith  @Date 2016年11月2016/11/22日 16:19
 */
class RealmHelper private constructor() {
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private var mSupportFragment: android.support.v4.app.Fragment? = null
    private var mRealm: Realm? = null

    companion object {
        fun get(activity: Activity): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mActivity = activity
            LifeCycleBinder.bind(activity, object: OnLifeCycleChangedListener {
                override fun onStart() {

                }

                override fun onStop() {

                }

                override fun onDestroy() {
                    realmHelper.mRealm?.close()
                }
            })
            return realmHelper
        }

        @SuppressLint("NewApi")
        fun get(fragment: Fragment): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mFragment = fragment

            LifeCycleBinder.bind(fragment, object: OnLifeCycleChangedListener {
                override fun onStart() {
                    realmHelper.mRealm = Realm.getDefaultInstance()
                }

                override fun onStop() {
                    realmHelper.mRealm?.close()
                }

                override fun onDestroy() {

                }
            })
            return realmHelper
        }

        fun get(supportFragment: android.support.v4.app.Fragment): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mSupportFragment = supportFragment


            LifeCycleBinder.bind(supportFragment, object: OnLifeCycleChangedListener {
                override fun onStart() {
                    realmHelper.mRealm = Realm.getDefaultInstance()
                }

                override fun onStop() {
                    realmHelper.mRealm?.close()
                }

                override fun onDestroy() {

                }
            })
            return realmHelper
        }
    }

    fun <T> use(f: Realm.() -> T): T {
        return openRealm().f()
    }

    fun <T> use(f: Realm.() -> T , openRealm: () -> Realm = {
        if(null == mRealm) {
            mRealm = Realm.getDefaultInstance()
        }
        mRealm!!
    }): T {
        return openRealm.invoke().f()
    }

    @Synchronized
    private fun openRealm(): Realm {
        if(null == mRealm) {
            mRealm = Realm.getDefaultInstance()
        }
        return mRealm!!
    }
}
