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
import okhttp3.Credentials
import okhttp3.ResponseBody

import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    private var mOktaAuth: OktaAppAuth? = null

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

    private fun onUserLogin(result: LoginResponse) {

        Session.userId = result.data.user.id
        auth_message.text = "Getting token..."

        var credentials = Credentials.basic(Config().client_id, Config().client_secret)

        loginClient.getToken(credentials, "password", "openid", email.text.toString(), password.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            Log.v("LoginActivity", "success")

                            Session.sessionToken = "Bearer " + result.access_token

                            val mainIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainIntent)

                            showFields()
                            auth_message.text = ""

                        },
                        { throwable ->
                            Toast.makeText(this, "Token get error: ${throwable.message}", Toast.LENGTH_LONG).show()
                            showFields()
                            auth_message.text = ""
                        }
                )



    }

    private fun clientLogin() {

        showProgress()
        auth_message.text = "Logging in..."

        loginClient.login(OktaUser("password", "openid", email.text.toString(), password.text.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            onUserLogin(result)
                        },
                        { throwable ->
                            Log.e("ERROR", throwable.message)
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

    }
}
