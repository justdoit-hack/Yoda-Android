package com.justdoit.yoda.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.justdoit.yoda.R
import com.justdoit.yoda.databinding.ItemMessageBinding
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.ui.MessageListFragmentDirections
import java.text.SimpleDateFormat


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
        holder.getBinding()?.setFromInAppPhoneNoText(entity.fromUser?.inAppPhoneNo ?: "#ﾋﾂｳﾁ")
        holder.getBinding()?.receiveTimeText = parseFromISO8601(entity.updatedAt)
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

    @SuppressLint("SimpleDateFormat")
    private fun parseFromISO8601(dateString: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
        val dt = df.parse(dateString)
        val df2 = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return df2.format(dt)
    }
}
