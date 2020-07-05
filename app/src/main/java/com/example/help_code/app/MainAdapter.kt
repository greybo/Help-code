package com.example.help_code.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.help_code.R
import kotlinx.android.synthetic.main.item_fragment_name.view.*

class MainAdapter(val listItmes: MutableList<String>, val callback: (String) -> Unit) :
    RecyclerView.Adapter<MainAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_name, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listItmes.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listItmes[position])
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: String) {
            itemView.mainfragmentText1.text = s
            itemView.mainfragmentText1.setOnClickListener {
                callback(s)
            }
        }
    }
}