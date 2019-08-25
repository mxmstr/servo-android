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
import com.google.zxing.integration.android.IntentResult
import com.platform.lynch.servo.adapter.TicketAdapter
import com.platform.lynch.servo.model.Session
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_scan.*
import kotlin.concurrent.schedule
import java.util.*




class ScanTab : TabFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.activity_scan, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnScan.setOnClickListener {
            run {
                if (!Session.seated)
                    IntentIntegrator(activity).initiateScan()
            }
        }
        btnLeave.setOnClickListener {
            run {
                if (Session.seated)
                    (activity as MainActivity).leaveTable()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)

        refresh()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        txtValue.text = if (!Session.seated) "None" else Session.table.id.toString()
    }

    companion object {

        fun getInstance(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("pos", position)
            val tabFragment = ScanTab()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }
}