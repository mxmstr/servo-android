package com.example.karl.oktakotlinretry

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_item.view.*
import android.app.Activity



class MenuAdapter(val activity: MainActivity) :
        RecyclerView.Adapter<MenuAdapter.MovieViewHolder>() {

    val client by lazy { MenuApiClient.create() }
    var items: ArrayList<MenuItem> = ArrayList()

    init { refresh() }

    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MenuAdapter.MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.name.text = items[position].name
        holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, items[position]) }
        holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, items[position]) }
    }

    override fun getItemCount() = items.size

    fun refresh() {
        client.get(activity.businessId!!)
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
        client.update(item.id, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun add(item: MenuItem) {
        client.add(activity.businessId!!, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun delete(item: MenuItem) {

        client.delete(item.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refresh() }, { throwable ->
                    Toast.makeText(activity.baseContext, "Delete error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )

    }

    fun showUpdateDialog(holder: MovieViewHolder, movie: MenuItem) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)

        val input = EditText(holder.view.context)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        input.setText(movie.name)

        dialogBuilder.setView(input)

        dialogBuilder.setTitle("Update Movie")
        dialogBuilder.setPositiveButton("Update", { dialog, whichButton ->
            update(MenuItem(movie.id,input.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    fun showDeleteDialog(holder: MovieViewHolder, movie: MenuItem) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Confirm delete?")
        dialogBuilder.setPositiveButton("Delete", { dialog, whichButton ->
            delete(movie)
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}
