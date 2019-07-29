package com.platform.lynch.servo.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.platform.lynch.servo.R
import kotlinx.android.synthetic.main.activity_main.*
import com.platform.lynch.servo.adapter.TabAdapter
import com.platform.lynch.servo.model.Config
import com.platform.lynch.servo.model.Table
import com.platform.lynch.servo.model.TableApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {

    val config: Config? = Config(
            "{CLIENT_ID}",
            "com.okta.dev-486832:/implicit/callback",
            "https://dev-486832.okta.com/oauth2/default",
            "http://10.4.2.14:8080/"
            )

    var userId: String? = null
    var table: Table? = null

    val tableClient by lazy { TableApiClient.create(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*resources.openRawResource(R.raw.okta_app_auth_config).apply {
            //val gson = Gson()
            //val map = HashMap<String, Any>()
            val gson = Gson()
            val config = gson.fromJson(this.readBytes().toString(Charsets.UTF_8), Config().javaClass)
            //val config = gson.fromJson(this.readBytes().toString(Charsets.UTF_8), map.javaClass) as HashMap<String, Any>
        }*/


        userId = intent.getStringExtra("UserId")


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        viewpager.adapter = TabAdapter(supportFragmentManager)
        tabs!!.setupWithViewPager(viewpager)


        Timer().schedule(timerTask {

            tableClient.get(userId!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            table = result[0]
                            txtValue.text = table!!.id.toString()
                        },
                        { error ->
                            Log.e("ERRORS", error.message)
                        }
                )

        }, 100)


    }

    fun leaveTable() {

        if (table == null)
            return

        table!!.customerId = null
        tableClient.update(table!!.id, table!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {  },//Toast.makeText(this@MainActivity, "Table left.", Toast.LENGTH_LONG).show() },
                        { throwable ->
                            Toast.makeText(this@MainActivity, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                        }
                )

        txtValue.text = "None"
        table = null

    }

    fun claimTable(result: Table) {

        if (result.customerId != "") {
            Toast.makeText(this@MainActivity, "This table is already claimed.", Toast.LENGTH_LONG).show()
            return
        }

        table = result
        table!!.customerId = userId!!

        tableClient.update(table!!.id, table!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Toast.makeText(this@MainActivity, "Table claimed.", Toast.LENGTH_LONG).show()
                            txtValue.text = table!!.id.toString()
                        },
                        { throwable ->
                            Toast.makeText(this@MainActivity, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                        }
                )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){

                tableClient.getById(result.contents.toLong())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result ->
                                    claimTable(result as Table)
                                },
                                { error ->
                                    Toast.makeText(this@MainActivity, "Get error: ${error.message}", Toast.LENGTH_LONG).show()
                                    Log.e("ERRORS", error.message)
                                }
                        )

            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}



//class MainActivity : AppCompatActivity() {
//
//    var userId: String? = null
//    var businessId: String? = null
//
//    lateinit var menuAdapter: MenuAdapter
//
//    fun readAuthState(): AuthState {
//        val authPrefs = getSharedPreferences("OktaAppAuthState", Context.MODE_PRIVATE)
//        val stateJson = authPrefs.getString("state", "")
//        return if (!stateJson!!.isEmpty()) {
//            try {
//                AuthState.jsonDeserialize(stateJson)
//            } catch (exp: org.json.JSONException) {
//                AuthState()
//            }
//
//        } else {
//            AuthState()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        userId = intent.getStringExtra("UserId")
//        businessId = intent.getStringExtra("BusinessId")
//
//
//        menuAdapter = MenuAdapter(this)
//
//        rv_item_list.layoutManager = LinearLayoutManager(this)
//        rv_item_list.adapter = menuAdapter
//
//        //fab.setOnClickListener{ showNewDialog() }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.buttons, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.refresh -> {
//            menuAdapter.refresh()
//            Toast.makeText(this.baseContext, "Refreshed", Toast.LENGTH_LONG).show()
//            true
//        }
//        else -> {
//            super.onOptionsItemSelected(item)
//        }
//    }
//
////    fun showNewDialog() {
////        val dialogBuilder = AlertDialog.Builder(this)
////
////        val layout = LinearLayout(this@MainActivity)
////        layout.layoutParams = LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.MATCH_PARENT,
////                LinearLayout.LayoutParams.MATCH_PARENT)
////        layout.orientation = LinearLayout.VERTICAL
////
////        val inputQuantity = EditText(this)
////        val inputOptions = EditText(this)
////        inputQuantity.hint = "Quantity"
////        inputOptions.hint = "Options"
////        layout.addView(inputQuantity)
////        layout.addView(inputOptions)
////
////        dialogBuilder.setView(layout)
////
////
////        dialogBuilder.setTitle("New Order")
////        dialogBuilder.setMessage("Enter Name Below")
////        dialogBuilder.setPositiveButton("Save", { dialog, whichButton ->
////            //menuAdapter.add(com.platform.lynch.servo.model.MenuItem(0, layout.text.toString()))
////        })
////        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
////            //pass
////        })
////        val b = dialogBuilder.create()
////        b.show()
////    }
//
//}
