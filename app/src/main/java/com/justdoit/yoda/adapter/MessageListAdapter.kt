package com.justdoit.yoda.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.justdoit.yoda.R
import com.justdoit.yoda.databinding.ItemMessageBinding
import com.justdoit.yoda.entity.MessageEntity


private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageEntity>() {
    override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean =
        oldItem.id == newItem.id
}

class MessageListAdapter : ListAdapter<MessageEntity, MessageListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBinding()?.entity = getItem(position)
    }

    // ViewHolder(固有ならインナークラスでOK)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mBinding: ItemMessageBinding? = null

        init {
            mBinding = DataBindingUtil.bind(itemView)
        }

        fun getBinding(): ItemMessageBinding? {
            return mBinding
        }
    }
}
