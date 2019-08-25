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
import com.platform.lynch.servo.model.Session
import com.platform.lynch.servo.model.Table
import com.platform.lynch.servo.model.TableApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {

    val tableClient by lazy { TableApiClient.create(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //userId = intent.getStringExtra("UserId")


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        viewpager.adapter = TabAdapter(supportFragmentManager)
        tabs!!.setupWithViewPager(viewpager)


        Timer().schedule(timerTask {

            tableClient.get(Session.sessionToken, Session.userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Session.table = result[0]
                            txtValue.text = Session.table.id.toString()
                        },
                        { error ->
                            Log.e("ERRORS", error.message)
                        }
                )

        }, 100)


    }

    fun leaveTable() {

        if (!Session.seated)
            return

        Session.table.customerId = null
        tableClient.update(Session.sessionToken, Session.table.id, Session.table).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            txtValue.text = "None"
                            Session.seated = false
                            Toast.makeText(this@MainActivity, "Table left.", Toast.LENGTH_LONG).show()
                        },
                        { throwable ->
                            Toast.makeText(this@MainActivity, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                        }
                )



    }

    fun claimTable(result: Table) {

        if (result.customerId != "") {
            Toast.makeText(this@MainActivity, "This table is already claimed.", Toast.LENGTH_LONG).show()
            return
        }

        Session.table = result
        Session.table.customerId = Session.userId

        tableClient.update(Session.sessionToken, Session.table.id, Session.table).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            txtValue.text = Session.table.id.toString()
                            Session.seated = true
                            Toast.makeText(this@MainActivity, "Table claimed.", Toast.LENGTH_LONG).show()
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

                tableClient.getById(Session.sessionToken, result.contents.toLong())
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