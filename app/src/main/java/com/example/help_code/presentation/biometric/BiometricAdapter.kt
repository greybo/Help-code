package com.example.help_code.presentation.biometric

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.help_code.databinding.BiometricAdapterItemSimpleBinding
import com.example.help_code.utilty.inflateAdapter

class BiometricAdapter(private val list: List<BiometricItems>, private val callback: (BiometricItems) -> Unit) :
    RecyclerView.Adapter<BiometricAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflateAdapter(BiometricAdapterItemSimpleBinding::inflate))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    inner class Holder(private val binding: BiometricAdapterItemSimpleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BiometricItems) {
            binding.itemText.text = item.itemText
            itemView.setOnClickListener {
                callback.invoke(item)
            }
        }
    }

}