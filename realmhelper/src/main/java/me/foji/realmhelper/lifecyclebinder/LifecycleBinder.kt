package me.foji.realmhelper.lifecyclebinder

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.os.Build

import java.util.ArrayList

/**
 * The life cycle of activity and fragment binding, it supports activity、android.app.Fragment、
 * android.support.v4.app.Fragment。
 *
 * Usage: LifecycleBinder.init().callback().addCallback().bind()
 *
 * @author Scott Smith  @Date 2016年11月2016/11/21日 18:30
 */
class LifecycleBinder private constructor() {
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private var mSupportFragment: android.support.v4.app.Fragment? = null
    private var mLifecycleListener: LifecycleListener? = null
    private val mLifecycleListeners = ArrayList<LifecycleListener>()
    private val FRAGMENT_TAG = "me.foji.lifecyclebinder.Fragment"

    fun callback(listener: LifecycleListener?): LifecycleBinder {
        mLifecycleListener = listener
        return this
    }

    fun addCallBack(listener: LifecycleListener): LifecycleBinder {
        mLifecycleListeners.add(listener)
        return this
    }

    fun bind() {
        // Bind activity
        if (null != mActivity) {
            assertNotDestroyed(mActivity!!)

            val fragmentManager = mActivity!!.fragmentManager
            var fragment: Fragment? = fragmentManager.findFragmentByTag(FRAGMENT_TAG)

            if (null == fragment) {
                fragment = LifecycleManageFragment(mLifecycleListener , mLifecycleListeners)
                fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss()
            }
        }

        // Bind fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (null != mFragment) {
                assertNotDestroyed(mFragment!!.activity)

                val fragmentManager = mFragment!!.childFragmentManager
                var fragment: Fragment? = fragmentManager.findFragmentByTag(FRAGMENT_TAG)

                if (null == fragment) {
                    fragment = LifecycleManageFragment(mLifecycleListener , mLifecycleListeners)
                    fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss()
                }
            }
        }

        // Bind support fragment
        if (null != mSupportFragment) {
            val activity = mSupportFragment!!.activity
            if (null != activity) {
                assertNotDestroyed(activity)

                val fragmentManager = activity.supportFragmentManager
                var fragment: android.support.v4.app.Fragment? = fragmentManager.findFragmentByTag(FRAGMENT_TAG)

                if (null == fragment) {
                    fragment = SupportLifecycleManageFragment(mLifecycleListener , mLifecycleListeners)
                    fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss()
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun assertNotDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            throw IllegalArgumentException("You cannot start a load for a destroyed activity")
        }
    }

    companion object {
        fun init(activity: Activity): LifecycleBinder {
            val binder = LifecycleBinder()
            binder.mActivity = activity
            return binder
        }

        fun init(fragment: Fragment): LifecycleBinder {
            val binder = LifecycleBinder()
            binder.mFragment = fragment
            return binder
        }

        fun init(fragment: android.support.v4.app.Fragment): LifecycleBinder {
            val binder = LifecycleBinder()
            binder.mSupportFragment = fragment
            return binder
        }
    }
}
