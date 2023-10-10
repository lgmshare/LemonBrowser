package com.leap.lemon.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.leap.idea.R
import com.leap.idea.databinding.ItemWebPageBinding
import com.leap.lemon.App
import com.leap.lemon.exts.dp2px
import com.leap.lemon.models.WebTab
import com.leap.lemon.utils.ScreenUtils

class WebPageAdapter(
    private val context: Context,
    private val itemClickListener: ((WebTab, Int) -> Unit)? = null,
    private val itemDeleteClickListener: ((WebTab, Int) -> Unit)? = null,
) : RecyclerView.Adapter<WebPageAdapter.ItemViewHolder>() {

    var dataList: ArrayList<WebTab> = arrayListOf()

    private var itemWidth: Int = 0
    private var itemHeight: Int = 0

    init {
        itemWidth = (ScreenUtils.getScreenWidth(context) - App.getInstance().dp2px(44f)) / 2
        itemHeight = (itemWidth * 1.3).toInt()
    }

    inner class ItemViewHolder(val binding: ItemWebPageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemWebPageBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataList[position]
        val bitmap = item.bitmap
        if (bitmap != null) {
            holder.binding.tabImage.setImageBitmap(bitmap)
        } else {
            holder.binding.tabImage.setImageResource(R.drawable.shape_ffffff_r12)
        }
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(item, position)
        }

        holder.binding.tabDelete.setOnClickListener {
            itemDeleteClickListener?.invoke(item, position)
        }

        holder.binding.tabDelete.isVisible = itemCount > 1
    }
}