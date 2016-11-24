package me.foji.realmdbutils

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.realm.RealmResults
import me.foji.realmhelper.RealmHelper
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var mNameEdit: EditText? = null
    private var mAgeEdit: EditText? = null
    private var mAddButton: Button? = null
    private var mListView: ListView? = null
    private var mAdapter: MyAdapter? = null
    private var mUpdateDialog: UpdateDialog? = null

    private var mRealmHelper: RealmHelper by Delegates.notNull<RealmHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRealmHelper = RealmHelper.get(this)

        mNameEdit = findViewById(R.id.edit_name) as? EditText
        mAgeEdit = findViewById(R.id.edit_age) as? EditText
        mAddButton = findViewById(R.id.button_add) as? Button
        mListView = findViewById(R.id.listView) as? ListView

        initListener()
        refresh()
    }

    private fun refresh() {
        mRealmHelper.use {
            val users = where(User::class.java).findAll()
            if(null != users) {
                if (null == mAdapter) {
                    mAdapter = MyAdapter()
                    mAdapter!!.users = users
                    mListView!!.adapter = mAdapter
                } else {
                    mAdapter!!.users = users
                    mAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initListener() {
        mAddButton!!.setOnClickListener {
            mRealmHelper.use {
                val id = where(User::class.java).max("id") as? Long?: 0
                executeTransactionAsync({ realm ->
                    val user = realm.createObject(User::class.java , id + 1)
                    user.name = mNameEdit!!.text.toString()
                    user.age = mAgeEdit!!.text.toString().toInt()
                } , {
                    refresh()
                } , { error ->
                    toast("Insert fail : $error")
                })
            }
        }

        mNameEdit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mAddButton!!.isEnabled = !TextUtils.isEmpty(mNameEdit!!.text.toString()) && !TextUtils.isEmpty(mAgeEdit!!.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mAgeEdit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mAddButton!!.isEnabled = !TextUtils.isEmpty(mNameEdit!!.text.toString()) && !TextUtils.isEmpty(mAgeEdit!!.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    inner class MyAdapter(): BaseAdapter() {
        var users: RealmResults<User>? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var viewHolder: MyViewHolder
            val view: View

            if(null == convertView) {
                view = layoutInflater.inflate(R.layout.list_item , parent , false)
                viewHolder = MyViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as MyViewHolder
            }

            viewHolder.txtUser.text = "${users!![position].name}\t\t${users!![position].age}"
            viewHolder.buttonUpdate.setOnClickListener {
                if(null == mUpdateDialog) {
                    mUpdateDialog = UpdateDialog(this@MainActivity)
                }
                mUpdateDialog!!.mUser = users!![position]
                mUpdateDialog!!.onUpdateSuccess = {
                    refresh()
                }
                if(!mUpdateDialog!!.isShowing) {
                    mUpdateDialog!!.show()
                }
            }
            viewHolder.buttonDelete.setOnClickListener {
                mRealmHelper.use {
                    executeTransaction {
                        users!![position].deleteFromRealm()
                        refresh()
                    }
                }
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return users!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return users!!.size
        }

    }

    inner class MyViewHolder(itemView: View) {
        private var itemView by Delegates.notNull<View>()
        var txtUser by Delegates.notNull<TextView>()
        var buttonUpdate by Delegates.notNull<Button>()
        var buttonDelete by Delegates.notNull<Button>()

        init {
            this.itemView = itemView

            txtUser = itemView.findViewById(R.id.txt_user) as TextView
            buttonUpdate = itemView.findViewById(R.id.button_update) as Button
            buttonDelete = itemView.findViewById(R.id.button_delete) as Button
        }
    }
}
