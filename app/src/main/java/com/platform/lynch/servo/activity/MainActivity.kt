package com.platform.lynch.servo.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.platform.lynch.servo.R
import com.platform.lynch.servo.adapter.MenuAdapter
import kotlinx.android.synthetic.main.activity_main.*
import net.openid.appauth.AuthState



class MainActivity : AppCompatActivity() {

    var userId: String? = null
    var businessId: String? = null

    lateinit var menuAdapter: MenuAdapter

    fun readAuthState(): AuthState {
        val authPrefs = getSharedPreferences("OktaAppAuthState", Context.MODE_PRIVATE)
        val stateJson = authPrefs.getString("state", "")
        return if (!stateJson!!.isEmpty()) {
            try {
                AuthState.jsonDeserialize(stateJson)
            } catch (exp: org.json.JSONException) {
                AuthState()
            }

        } else {
            AuthState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra("UserId")
        businessId = intent.getStringExtra("BusinessId")


        menuAdapter = MenuAdapter(this)

        rv_item_list.layoutManager = LinearLayoutManager(this)
        rv_item_list.adapter = menuAdapter

        //fab.setOnClickListener{ showNewDialog() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.buttons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            menuAdapter.refresh()
            Toast.makeText(this.baseContext, "Refreshed", Toast.LENGTH_LONG).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

//    fun showNewDialog() {
//        val dialogBuilder = AlertDialog.Builder(this)
//
//        val layout = LinearLayout(this@MainActivity)
//        layout.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT)
//        layout.orientation = LinearLayout.VERTICAL
//
//        val inputQuantity = EditText(this)
//        val inputOptions = EditText(this)
//        inputQuantity.hint = "Quantity"
//        inputOptions.hint = "Options"
//        layout.addView(inputQuantity)
//        layout.addView(inputOptions)
//
//        dialogBuilder.setView(layout)
//
//
//        dialogBuilder.setTitle("New Order")
//        dialogBuilder.setMessage("Enter Name Below")
//        dialogBuilder.setPositiveButton("Save", { dialog, whichButton ->
//            //menuAdapter.add(com.platform.lynch.servo.model.MenuItem(0, layout.text.toString()))
//        })
//        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
//            //pass
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }

}
