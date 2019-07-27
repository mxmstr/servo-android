package com.platform.lynch.servo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.okta.appauth.android.OktaAppAuth

import android.widget.Toast
import com.platform.lynch.servo.*
import com.platform.lynch.servo.model.LoginApiClient
import com.platform.lynch.servo.model.LoginResponse
import com.platform.lynch.servo.model.User
import com.platform.lynch.servo.model.UserInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
