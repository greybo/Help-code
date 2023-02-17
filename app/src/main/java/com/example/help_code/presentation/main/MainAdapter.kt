package com.example.help_code.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.help_code.R
import kotlinx.android.synthetic.main.item_fragment_name.view.*

class MainAdapter(enumItem: Array<FragmentName>, val callback: (FragmentName) -> Unit) :
    RecyclerView.Adapter<MainAdapter.Holder>() {

    private val listItems: List<FragmentName> = enumItem.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_name, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listItems[position])
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: FragmentName) {
            itemView.mainfragmentText1.text = s.rawValue
            itemView.mainfragmentText1.setOnClickListener {
                callback(s)
            }
        }
    }
}