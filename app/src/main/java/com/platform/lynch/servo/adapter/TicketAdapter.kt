package com.platform.lynch.servo.adapter

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.platform.lynch.servo.activity.MainActivity
import com.platform.lynch.servo.model.MenuApiClient
import com.platform.lynch.servo.model.MenuItem
import com.platform.lynch.servo.R
import com.platform.lynch.servo.model.Ticket
import com.platform.lynch.servo.model.TicketApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.ticket_item.view.*
import kotlinx.coroutines.runBlocking


class TicketAdapter(val activity: Activity) :
        RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    val ticketClient by lazy { TicketApiClient.create(activity) }
    var tickets: ArrayList<Ticket> = ArrayList()

    init { refresh() }

    class TicketViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TicketViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_item, parent, false)

        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.view.itemName.text = tickets[position].itemName
        holder.view.timestamp.text = tickets[position].timestamp
        holder.view.status.text = tickets[position].status
    }

    override fun getItemCount() = tickets.size

    fun refresh() {
            ticketClient.get((activity as MainActivity)?.userId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                tickets.clear()
                                tickets.addAll(result)
                                notifyDataSetChanged()
                                Toast.makeText(activity.baseContext, "Refreshed", Toast.LENGTH_LONG).show()
                            },
                            { error ->
                                Toast.makeText(activity.baseContext, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                                Log.e("ERRORS", error.message)
                            }
                    )
    }

}
