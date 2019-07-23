package com.example.karl.oktakotlinretry

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_login.*
import com.okta.appauth.android.OktaAppAuth
import net.openid.appauth.AuthorizationException
import java.lang.reflect.Field

import com.okta.sdk.authc.credentials.TokenClientCredentials
import com.okta.sdk.client.Client
import com.okta.sdk.client.Clients
import com.okta.sdk.resource.ResourceException
import android.R.string.ok
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Arrays.asList
import com.okta.sdk.resource.user.UserBuilder
import com.okta.sdk.resource.user.User
import org.json.JSONObject

import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    private var mOktaAuth: OktaAppAuth? = null
    private var credentials: UserInfo? = null

    val client by lazy { LoginApiClient.create() }


    fun onUserLogin(result: Response<LoginResponse>) {

        credentials = result.body()!!.data.user

        Log.v("LoginActivity", "success")

        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("UserId", credentials!!.id)
        mainIntent.putExtra("BusinessId", "00uoudimbcBaMgXYj356")

        startActivity(mainIntent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mOktaAuth = OktaAppAuth.getInstance(this)

        setContentView(R.layout.activity_login)

        val client by lazy { LoginApiClient.create() }

        client.getUser(User("lynch.er18+test0@gmail.com", "aaaAAA1!"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            onUserLogin(result)
                        },
                        { throwable ->
                            Toast.makeText(this, "Login error: ${throwable.message}", Toast.LENGTH_LONG).show()
                        }
                )

//        val oktaUser = UserBuilder.instance()
//                .setEmail("lynch.er18@gmail.com")
//                .setPassword("65486541gG#".toCharArray())
//                .buildAndCreate(client)

//        Log.e("LoginActivity", oktaUser.id)
//
//            val result = Business()


        /*mOktaAuth!!.init(
                this,
                object : OktaAppAuth.OktaAuthListener {
                    override fun onSuccess() {
                        Log.v("LoginActivity", "Success")
                        auth_button.visibility = View.VISIBLE
                        auth_message.visibility = View.GONE
                        progress_bar.visibility = View.GONE
                    }

                    override fun onTokenFailure(ex: AuthorizationException) {
                        Log.v("LoginActivity", ex.toString())
                        auth_message.text = ex.toString()
                        progress_bar.visibility = View.GONE
                        auth_button.visibility = View.GONE
                    }
                }
        )

        val button = findViewById(R.id.auth_button) as Button
        button.setOnClickListener { v ->
            val completionIntent = Intent(v.context, MainActivity::class.java)
            val cancelIntent = Intent(v.context, LoginActivity::class.java)

            cancelIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


            mOktaAuth!!.login(
                    v.context,
                    PendingIntent.getActivity(v.context, 0, completionIntent, 0),
                    PendingIntent.getActivity(v.context, 0, cancelIntent, 0)
            )
        }*/
    }
}
