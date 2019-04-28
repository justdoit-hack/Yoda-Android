package com.justdoit.yoda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justdoit.yoda.R
import com.justdoit.yoda.databinding.FragmentSendBinding
import com.justdoit.yoda.viewmodel.SendViewModel

class SendFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SendViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSendBinding>(inflater, R.layout.fragment_send, container, false)
        return binding.root
    }
}
