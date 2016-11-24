# Realm Helper
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
