package com.justdoit.yoda.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.justdoit.yoda.GeneralSystem
import com.justdoit.yoda.R
import com.justdoit.yoda.databinding.ItemMessageBinding
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.entity.SourceTypeEnum
import com.justdoit.yoda.ui.MessageListFragmentDirections

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
        val entity = getItem(position)
        holder.getBinding()?.entity = entity
        val phoneText = entity.fromUser?.inAppPhoneNo ?: "非通知"
        holder.getBinding()?.setFromInAppPhoneNoText("# $phoneText")
        holder.getBinding()?.receiveTimeText = GeneralSystem.parseFromISO8601(entity.updatedAt)

        if (entity.sourceType == SourceTypeEnum.API) {
            // スマホアプリ
            holder.getBinding()?.fromUserIcon?.setImageResource(R.drawable.ic_user_api)
        } else if (entity.sourceType == SourceTypeEnum.ASTERISK) {
            // 固定電話
            holder.getBinding()?.fromUserIcon?.setImageResource(R.drawable.ic_user_phone)
        } else if (entity.sourceType == SourceTypeEnum.ANONYMOUS) {
            // 非通知
            holder.getBinding()?.fromUserIcon?.setImageResource(R.drawable.ic_user_none)
        }

        holder.getBinding()?.frameMessage?.setBackgroundResource(R.drawable.frame_black)
        holder.getBinding()?.messageText?.setTextColor(Color.parseColor("#333333"))
        holder.getBinding()?.translateBtn?.setOnClickListener {
            if (holder.getBinding()?.messageText?.text == entity.originalBody) {
                holder.getBinding()?.frameMessage?.setBackgroundResource(R.drawable.frame_light)
                holder.getBinding()?.messageText?.setTextColor(Color.parseColor("#C6B399"))
                holder.getBinding()?.messageText?.text = entity.parsed
            } else {
                holder.getBinding()?.frameMessage?.setBackgroundResource(R.drawable.frame_black)
                holder.getBinding()?.messageText?.setTextColor(Color.parseColor("#333333"))
                holder.getBinding()?.messageText?.text = entity.originalBody
            }
        }

        holder.getBinding()?.itemMessage?.setOnClickListener {
            val fragment = MessageListFragmentDirections.actionListFragmentToSendFragment(entity)
            Navigation.findNavController(it).navigate(fragment)
        }
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
