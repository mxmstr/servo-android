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
import com.platform.lynch.servo.R
import com.platform.lynch.servo.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.menu_item.view.*


class MenuAdapter(val activity: Activity) :
        RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    val menuClient by lazy { MenuApiClient.create(activity) }
    val ticketClient by lazy { TicketApiClient.create(activity) }
    var items: ArrayList<MenuItem> = ArrayList()

    init { refresh() }

    class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MenuViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.menu_item, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.view.name.text = items[position].name
        holder.view.price.text = items[position].price
        holder.view.btnAdd.setOnClickListener { showAddDialog(holder, items[position]) }
        //holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, items[position]) }
        //holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, items[position]) }
    }

    override fun getItemCount() = items.size

    fun refresh() {
        if (!Session.seated)
            return

        menuClient.get(Session.sessionToken, Session.table.businessId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            items.clear()
                            items.addAll(result)
                            notifyDataSetChanged()
                        },
                        { error ->
                            Toast.makeText(activity.baseContext, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                            Log.e("ERRORS", error.message)
                        }
                )
    }

    fun update(item: MenuItem) {
        menuClient.update(Session.sessionToken, item.id, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun add(item: MenuItem) {
        if (!Session.seated)
            return

        menuClient.add(Session.sessionToken, Session.table.businessId, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun delete(item: MenuItem) {

        menuClient.delete(Session.sessionToken, item.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Delete error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )

    }

    fun placeOrder(item: Ticket) {
        if (!Session.seated)
            return

        ticketClient.add(Session.sessionToken, Session.userId, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            refresh()
                            Toast.makeText(activity.baseContext, "Order placed.", Toast.LENGTH_LONG).show()
                        },
                        { throwable ->
                            Toast.makeText(activity.baseContext, "Order error: ${throwable.message}", Toast.LENGTH_LONG).show()
                        }
                )

    }

    fun showAddDialog(holder: MenuViewHolder, item: MenuItem) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)

        val layout = LinearLayout(holder.view.context)
        layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        layout.orientation = LinearLayout.VERTICAL

        val inputQuantity = EditText(holder.view.context)
        val inputOptions = EditText(holder.view.context)
        inputQuantity.hint = "Quantity"
        inputOptions.hint = "Options"
        layout.addView(inputQuantity)
        layout.addView(inputOptions)

        dialogBuilder.setView(layout)
        dialogBuilder.setTitle("Place Order: " + item.name)
        dialogBuilder.setMessage(item.price)
        dialogBuilder.setPositiveButton("Confirm", { dialog, whichButton -> placeOrder(
                Ticket(0,
                    Session.userId,
                    item.id,
                    item.name,
                    inputQuantity.text.toString(),
                    inputOptions.text.toString(),
                    "",
                    "OPEN"
                    ))
                })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton -> dialog.cancel() })
        val b = dialogBuilder.create()
        b.show()
    }

    fun showUpdateDialog(holder: MenuViewHolder, item: MenuItem) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)

        val input = EditText(holder.view.context)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        input.setText(item.name)

        dialogBuilder.setView(input)

        dialogBuilder.setTitle("Update Item")
        dialogBuilder.setPositiveButton("Update", { dialog, whichButton ->
            update(MenuItem(item.id, input.text.toString(), item.price))
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    fun showDeleteDialog(holder: MenuViewHolder, item: MenuItem) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Confirm delete?")
        dialogBuilder.setPositiveButton("Delete", { dialog, whichButton ->
            delete(item)
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}
