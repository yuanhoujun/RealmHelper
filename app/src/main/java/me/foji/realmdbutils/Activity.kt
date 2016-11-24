package me.foji.realmdbutils

import android.app.Activity
import android.widget.Toast

/**
 * @author Scott Smith  @Date 2016年11月2016/11/24日 10:28
 */
fun Activity.toast(text: CharSequence? , length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this , text , length).show()
}