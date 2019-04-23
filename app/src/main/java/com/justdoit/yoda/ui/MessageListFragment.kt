package com.justdoit.yoda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.justdoit.yoda.adapter.MessageListAdapter
import com.justdoit.yoda.databinding.FragmentListBinding
import com.justdoit.yoda.viewmodel.MessageListViewModel


class MessageListFragment : Fragment() {
    private val viewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MessageListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate(
            inflater,
            com.justdoit.yoda.R.layout.fragment_list,
            container,
            false
        ) as FragmentListBinding

        val adapter = MessageListAdapter()
        viewModel.items?.observe(this, Observer { list ->
            adapter.submitList(list)
        })

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.messageList.layoutManager = linearLayoutManager
        binding.messageList.adapter = adapter

//        val itemsObserver = Observer<List<MessageEntity>> { users ->
//            // Update the UI, in this case, a TextView.
//            users?.forEach {
//                // todo データをリストに反映させる処理
//
//                Log.d("YODA_ID", it.userId)
//            }
//        }
//
//        viewModel.items.observe(this, itemsObserver)

        return binding.root
    }
}
