package me.foji.realmhelper.lifecyclebinder

import android.app.Fragment
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Scott Smith  @Date 2016年11月2016/11/21日 18:45
 */
class LifecycleManageFragment(lifecycleListener: LifecycleListener? , listeners: ArrayList<LifecycleListener>) : Fragment() {
    private var mLifecycleListener: LifecycleListener? = null
    private var mLifecycleListeners: ArrayList<LifecycleListener> by Delegates.notNull<ArrayList<LifecycleListener>>()

    constructor(): this(null , ArrayList())

    init {
        mLifecycleListener = lifecycleListener
        mLifecycleListeners = listeners
    }

    override fun onStart() {
        super.onStart()
        mLifecycleListener?.onStart()
        for(listener in mLifecycleListeners) {
            listener.onStart()
        }
    }

    override fun onStop() {
        super.onStop()
        mLifecycleListener?.onStop()
        for(listener in mLifecycleListeners) {
            listener.onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLifecycleListener?.onDestroy()
        for(listener in mLifecycleListeners) {
            listener.onDestroy()
        }
    }
}
