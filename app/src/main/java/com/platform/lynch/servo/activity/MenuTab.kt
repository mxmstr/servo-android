package com.platform.lynch.servo.activity

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.platform.lynch.servo.R
import android.view.LayoutInflater
import android.widget.Toast
import com.platform.lynch.servo.adapter.MenuAdapter
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync
import kotlin.system.measureTimeMillis


class MenuTab : TabFragment() {

    lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        menuAdapter = MenuAdapter(activity)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        return inflater!!.inflate(R.layout.activity_menu, container, false)
    }

    private fun waitForTable() {
        while ((activity as MainActivity).table == null) {}
    }

    override fun onViewCreated(view: View?, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAdapter.refresh()

        rv_item_list?.layoutManager = LinearLayoutManager(activity)
        rv_item_list?.adapter = menuAdapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.buttons, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            menuAdapter.refresh()
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
            val tabFragment = MenuTab()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }
}