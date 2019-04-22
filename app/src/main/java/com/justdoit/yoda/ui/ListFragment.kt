package com.justdoit.yoda.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.justdoit.yoda.R
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private val viewModel: ListViewModel by lazy { ViewModelProviders.of(activity!!).get(ListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemsObserver = Observer<List<MessageEntity>> { users ->
            // Update the UI, in this case, a TextView.
            users?.forEach {
                // todo データをリストに反映させる処理

                Log.d("YODA_ID", it.userId)
            }
        }

        viewModel.items.observe(this, itemsObserver)

        // todo databinding
        return inflater.inflate(R.layout.fragment_list, container, false)
    }
}
