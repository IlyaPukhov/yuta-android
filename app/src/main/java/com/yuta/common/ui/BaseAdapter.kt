package com.yuta.common.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseAdapter.ViewHolder<T>>(
    val items: MutableList<T>
) : RecyclerView.Adapter<VH>() {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    @Suppress("NotifyDataSetChanged")
    fun refillList(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: T) {
        val position = items.indexOf(item)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    fun insertItem(item: T) {
        items.add(item)
        val position = items.indexOf(item)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemCount(): Int = items.size

    abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}
