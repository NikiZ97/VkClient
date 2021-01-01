package com.sharonovnik.vkclient.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sharonovnik.vkclient.R
import kotlinx.android.synthetic.main.layout_user_info_item.view.*

class UserInfoAdapter: RecyclerView.Adapter<UserInfoAdapter.UserInfoHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<UserInfoItem>() {
        override fun areItemsTheSame(oldItem: UserInfoItem, newItem: UserInfoItem): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: UserInfoItem, newItem: UserInfoItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var items: List<UserInfoItem> = emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                differ.submitList(value)
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoHolder {
        return UserInfoHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user_info_item, parent, false))
    }

    override fun onBindViewHolder(holder: UserInfoHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class UserInfoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserInfoItem) {
            val section = item.section
            val headerIsVisible: Boolean
            headerIsVisible = when {
                section == null -> {
                    false
                }
                adapterPosition == 0 -> {
                    true
                }
                else -> {
                    val previous = items[adapterPosition - 1]
                    section != previous.section
                }
            }

            if (headerIsVisible) {
                itemView.header.isVisible = true
                itemView.header.text = itemView.context.getString(section!!.titleResId)
            } else {
                itemView.header.isVisible = false
            }

            itemView.icon.setImageDrawable(ContextCompat.getDrawable(itemView.context, item.iconResId))
            itemView.title.text = item.title.getString(itemView.context)
            itemView.subtitle.text = item.subtitle
        }
    }
}