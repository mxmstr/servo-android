package com.platform.lynch.servo.activity

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.platform.lynch.servo.R
import android.view.LayoutInflater
import android.widget.Toast
import com.platform.lynch.servo.adapter.TicketAdapter
import kotlinx.android.synthetic.main.activity_menu.*
import kotlin.concurrent.schedule
import java.util.*


class TicketTab : TabFragment() {

    lateinit var ticketAdapter: TicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ticketAdapter = TicketAdapter(activity)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        return inflater!!.inflate(R.layout.activity_menu, container, false)
    }

    override fun onViewCreated(view: View?, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketAdapter.refresh()

        rv_item_list?.layoutManager = LinearLayoutManager(activity)
        rv_item_list?.adapter = ticketAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.buttons, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            ticketAdapter.refresh()
            Toast.makeText(context, "Refreshed", Toast.LENGTH_LONG).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        fun getInstance(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("pos", position)
            val tabFragment = TicketTab()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }
}