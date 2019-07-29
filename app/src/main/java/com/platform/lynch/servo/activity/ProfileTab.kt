package com.platform.lynch.servo.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.platform.lynch.servo.R
import android.view.LayoutInflater
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_profile.*
import kotlin.concurrent.schedule
import java.util.*


class ProfileTab : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            run {
                activity.finish()
            }
        }
    }

    fun refresh() {
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)

        refresh()
    }

    companion object {

        fun getInstance(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("pos", position)
            val tabFragment = ProfileTab()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }
}