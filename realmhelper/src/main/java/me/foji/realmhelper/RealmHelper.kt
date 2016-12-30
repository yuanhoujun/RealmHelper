package me.foji.realmhelper

import android.app.Activity
import android.app.Fragment
import io.realm.Realm
import me.foji.realmhelper.lifecyclebinder.LifecycleBinder
import me.foji.realmhelper.lifecyclebinder.LifecycleListener

/**
 * Realm helper
 *
 * @author Scott Smith  @Date 2016年11月2016/11/22日 16:19
 */
class RealmHelper private constructor(): LifecycleListener {
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private var mSupportFragment: android.support.v4.app.Fragment? = null
    private var mRealm: Realm? = null

    companion object {
        fun get(activity: Activity): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mActivity = activity

            LifecycleBinder.init(realmHelper.mActivity!!).callback(realmHelper).bind()

            return realmHelper
        }

        fun get(fragment: Fragment): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mFragment = fragment

            LifecycleBinder.init(fragment).callback(realmHelper).bind()

            return realmHelper
        }

        fun get(supportFragment: android.support.v4.app.Fragment): RealmHelper {
            val realmHelper = RealmHelper()
            realmHelper.mSupportFragment = supportFragment

            LifecycleBinder.init(supportFragment).callback(realmHelper).bind()

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

    override fun onStart() {
        if(null != mFragment || null != mSupportFragment) {
            mRealm = Realm.getDefaultInstance()
        }
    }

    override fun onStop() {
        if(null != mFragment || null != mSupportFragment) {
            mRealm?.close()
        }
    }

    override fun onDestroy() {
        if(null != mActivity) {
            mRealm?.close()
        }
    }
}
