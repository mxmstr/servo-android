package com.platform.lynch.servo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.okta.appauth.android.OktaAppAuth

import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.platform.lynch.servo.*
import com.platform.lynch.servo.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_scan.*
import okhttp3.ResponseBody

import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    val config: Config? = Config(
            "{CLIENT_ID}",
            "com.okta.dev-486832:/implicit/callback",
            "https://dev-486832.okta.com/oauth2/default",
            "http://192.168.0.18:8080/"
    )

    private var mOktaAuth: OktaAppAuth? = null
    private var credentials: UserInfo? = null

    val loginClient by lazy { LoginApiClient.create(this) }
    val registerClient by lazy { RegisterApiClient.create(this) }


    private fun showFields() {

        email.visibility = View.VISIBLE
        password.visibility = View.VISIBLE
        login.visibility = View.VISIBLE
        register.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE

    }

    private fun showProgress() {

        email.visibility = View.GONE
        password.visibility = View.GONE
        login.visibility = View.GONE
        register.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE

    }

    private fun onUserLogin(result: LoginData) {

        credentials = result.user

        Log.v("LoginActivity", "success")

        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("UserId", credentials!!.id)

        startActivity(mainIntent)

    }

    private fun clientLogin() {

        showProgress()
        auth_message.text = "Logging in..."

        loginClient.getUser(OktaUser(email.text.toString(), password.text.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            onUserLogin(result.data)
                            showFields()
                            auth_message.text = ""
                        },
                        { throwable ->
                            Toast.makeText(this, "Login error: ${throwable.message}", Toast.LENGTH_LONG).show()
                            showFields()
                            auth_message.text = ""
                        }
                )

    }

    private fun clientRegister() {

        showProgress()
        auth_message.text = "Registering..."

        registerClient.createUser(User(email.text.toString(), password.text.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            clientLogin()
                            showFields()
                            auth_message.text = ""
                        },
                        { throwable ->
                            Toast.makeText(this, "Register error.", Toast.LENGTH_LONG).show()
                            showFields()
                            auth_message.text = ""
                        }
                )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mOktaAuth = OktaAppAuth.getInstance(this)

        setContentView(R.layout.activity_login)


        login.setOnClickListener {
            run {
                clientLogin()
            }
        }

        register.setOnClickListener {
            run {
                clientRegister()
            }
        }

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
