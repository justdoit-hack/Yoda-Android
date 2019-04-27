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
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.adapter.MessageListAdapter
import com.justdoit.yoda.databinding.FragmentListBinding
import com.justdoit.yoda.viewmodel.MessageListViewModel


class MessageListFragment : Fragment() {

    private val viewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(MessageListViewModel::class.java)
    }

    private var limit: Int? = null
    private var offset: Int? = null
    private var authToken: String? = null

    private val adapter = MessageListAdapter()

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

        val sessionManager = SessionManager.instance
        authToken = sessionManager.authToken

        observeMessageList(limit, offset)

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.messageList.layoutManager = linearLayoutManager
        binding.messageList.adapter = adapter

        binding.addBtn.setOnClickListener {
            observeMessageList(limit, offset)
        }

        return binding.root
    }

    private fun observeMessageList(limit: Int?, offset: Int?) {
        authToken?.let {
            viewModel.getMessageList(limit, offset, it).observe(this, Observer { list ->
                adapter.submitList(list)
            })
        }


    }

}
