package com.sharonovnik.vkclient.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerBindableAdapter<T, V: RecyclerView.ViewHolder> (
    private var items: List<T>,
    diffCallback: DiffUtil.ItemCallback<T>
) : RecyclerView.Adapter<V>() {
    
    private val headers = mutableListOf<View>()
    var layoutManager: RecyclerView.LayoutManager? = null
    private var inflater: LayoutInflater? = null
    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager
        }
        if (inflater == null) {
            inflater = LayoutInflater.from(recyclerView.context)
        }
    }

    override fun getItemCount(): Int {
        return headers.size + items.size
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun setItems(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeader(position)) {
            return TYPE_HEADER
        }
        val type = getItemType(position)
        if (type == TYPE_HEADER) {
            throw IllegalArgumentException("Item type can't be $TYPE_HEADER")
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        if (viewType != TYPE_HEADER) {
            return onCreateItemViewHolder(parent, viewType)
        }
        val frameLayout = FrameLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return HeaderViewHolder(frameLayout) as V
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        if (isHeader(position)) {
            val view = headers[position]
            prepareHeader(holder as HeaderViewHolder, view)
        } else {
            onBindItemViewHolder(holder, position - headers.size, getItemType(position))
        }
    }

    private fun isHeader(position: Int) = (position < headers.size)

    private fun prepareHeader(holder: HeaderViewHolder, view: View) {
        view.parent?.let {
            (it as ViewGroup).removeView(view)
        }
        with (holder.itemView as ViewGroup) {
            removeAllViews()
            addView(view)
        }
    }

    protected fun onCreateItemViewHolder(parent: ViewGroup, type: Int): V {
        return viewHolder(inflater!!.inflate(layoutId(type), parent, false), type)
    }

    fun getItemType(position: Int) = 0

    abstract fun layoutId(type: Int): Int

    abstract fun viewHolder(view: View, type: Int): V

    abstract fun onBindItemViewHolder(viewHolder: V, position: Int, type: Int)

    fun addHeader(header: View) {
        if (header !in headers) {
            headers += header
            notifyItemInserted(headers.size - 1)
        }
    }

    companion object {
        const val TYPE_HEADER = 123

        class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    }

}