package me.foji.realmdbutils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import me.foji.realmhelper.RealmHelper
import kotlin.properties.Delegates

/**
 * @author Scott Smith  @Date 2016年11月2016/11/24日 11:03
 */
class UpdateDialog(context: Context) : Dialog(context) {
    var mUser: User by Delegates.notNull<User>()
    private var mNameEdit by Delegates.notNull<EditText>()
    private var mAgeEdit by Delegates.notNull<EditText>()
    private var mUpdateButton by Delegates.notNull<Button>()
    private var mRealmHelper: RealmHelper by Delegates.notNull<RealmHelper>()
    var onUpdateSuccess: ((user: User) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_update)

        mNameEdit = findViewById(R.id.edit_name) as EditText
        mAgeEdit = findViewById(R.id.edit_age) as EditText
        mUpdateButton = findViewById(R.id.button_update) as Button

        mRealmHelper = RealmHelper.Companion.get(context as Activity)

        mUpdateButton.setOnClickListener {
            mRealmHelper.use {
                executeTransaction {
                    mUser.name = mNameEdit.text.toString()
                    mUser.age = mAgeEdit.text.toString().toInt()
                    try {
                        insertOrUpdate(mUser)
                        context.toast("Update success")
                        dismiss()
                        onUpdateSuccess?.invoke(mUser)
                    } catch(e: Exception) {
                    }
                }
            }
        }

        mNameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mUpdateButton.isEnabled = !TextUtils.isEmpty(mNameEdit.text.toString()) && !TextUtils.isEmpty(mAgeEdit.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mAgeEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mUpdateButton.isEnabled = !TextUtils.isEmpty(mNameEdit.text.toString()) && !TextUtils.isEmpty(mAgeEdit.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun show() {
        mNameEdit.setText(mUser.name)
        mAgeEdit.setText("${mUser.age}")

        super.show()
    }
}