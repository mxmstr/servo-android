package com.platform.lynch.servo.activity

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.platform.lynch.servo.R
import kotlinx.android.synthetic.main.fragment_tab.*


abstract class TabFragment : Fragment() {

    internal var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = getArguments().getInt("pos")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                     savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_tab, container, false)
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

}