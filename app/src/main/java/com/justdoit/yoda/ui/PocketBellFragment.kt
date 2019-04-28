package com.justdoit.yoda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.justdoit.yoda.R
import com.justdoit.yoda.databinding.FragmentPocketBellBinding

class PocketBellFragment : Fragment() {

    lateinit var binding: FragmentPocketBellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pocket_bell,
            container,
            false
        ) as FragmentPocketBellBinding

        // Inflate the layout for this fragment
        return binding.root
    }

    // ここはviewmodel使えばいらなさそう
    private fun changeDisplay(text: String, colorResource: Int) {
        binding.displayPocketBell.setBackgroundColor(colorResource)
        binding.displayPocketBell.text = text
    }
}
