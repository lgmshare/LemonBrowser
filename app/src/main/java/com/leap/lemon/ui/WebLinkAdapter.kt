package com.leap.lemon.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leap.idea.databinding.ItemWebLinkBinding
import com.leap.lemon.models.WebLink

class WebLinkAdapter(private val context: Context, private val itemClickListener: ((WebLink, Int) -> Unit)? = null) :
    RecyclerView.Adapter<WebLinkAdapter.ItemViewHolder>() {

    var dataList: ArrayList<WebLink> = arrayListOf()

    inner class ItemViewHolder(val binding: ItemWebLinkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemWebLinkBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.linkLogo.setImageResource(item.icon)
        holder.binding.linkName.text = item.name
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(item, position)
        }
    }
}