# Realm helper by Kotlin
----
Realm helper is aim to simplify realm database use .

The realm of Android version still have some troublesome problems, such as:

* You must remember to call close method in onDestroy method of activity or fragment
* It can’t call each other betweeten threads

For problem one, Realm helper has provided a solution. It can use realm database more easily ,  and help you to auto close realm in onDestroy method.

<pre>
class MainActivity : AppCompatActivity() {
    private var mRealmHelper: RealmHelper by Delegates.notNull<RealmHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		 
		 // You must 	ensure activity has been created
        mRealmHelper = RealmHelper.get(this)
        
        // You can use all method in Realm class at here
        mRealmHelper.use { 
            executeTransaction { 
                
            }
        }
    }
}
</pre>
Watch more, checkout this project , see demo module. 

For problem two, it still hav’nt a good solution , you must be carefull when call in multiple threads


This project will keep updating, if you find some other troublesome problem, just send email: [ouyangfeng2016@gmail.com]() to me 

# About me
**笔名**: 欧阳锋

**爱好**: 编程,旅游,运动

**擅长**: Android/iOS/Java Web/C++/Kotlin

**简书**: [关注我的简书](http://www.jianshu.com/users/db019edd34b4/latest_articles)

**签名**: 做一个有理想的程序员

**iOS交流群**: 468167089

**Kotlin交流群**: 329673958

**喜欢编程的同学,别忘了关注我的简书哦!**