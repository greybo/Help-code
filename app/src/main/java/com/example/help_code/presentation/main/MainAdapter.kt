package com.example.help_code.presentation.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.help_code.databinding.ItemFragmentNameBinding
import com.example.help_code.utilty.inflateAdapter

class MainAdapter(enumItem: Array<FragmentNameEnum>, val callback: (FragmentNameEnum) -> Unit) :
    RecyclerView.Adapter<MainAdapter.Holder>() {

    private val listItems: List<FragmentNameEnum> = enumItem.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflateAdapter(ItemFragmentNameBinding::inflate))
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listItems[position])
    }

    inner class Holder(val binding: ItemFragmentNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(s: FragmentNameEnum) {
            binding.mainfragmentText1.text = s.rawValue
            binding.mainfragmentText1.setOnClickListener {
                callback(s)
            }
        }
    }
}