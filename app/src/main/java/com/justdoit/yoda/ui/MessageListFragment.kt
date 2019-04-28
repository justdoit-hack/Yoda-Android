package com.justdoit.yoda.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.adapter.MessageListAdapter
import com.justdoit.yoda.databinding.FragmentListBinding
import com.justdoit.yoda.viewmodel.MessageListViewModel


class MessageListFragment : Fragment() {

    private val viewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(MessageListViewModel::class.java)
    }

    lateinit var binding: FragmentListBinding

    private var limit: Int? = null
    private var offset: Int? = null
    private var authToken: String? = null

    private val messageListAdapter = MessageListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        ) as FragmentListBinding

        binding.viewModel = viewModel

        val sessionManager = SessionManager.instance
        authToken = sessionManager.authToken

        authToken?.let {
            viewModel.item.observe(this, Observer { list ->
                beginningWorld()
                messageListAdapter.submitList(list)
            })
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.messageList.apply {
            layoutManager = linearLayoutManager
            adapter = messageListAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if ((newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_DRAGGING) && firstPosition != 0) {
                        binding.fab.hide()
                    } else {
                        binding.fab.show()
                    }
                }
            })
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchMessageList(limit, offset)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    private fun fetchMessageList(limit: Int?, offset: Int?) {
        authToken?.let {
            viewModel.getMessageList(limit, offset, it)
        }
    }

    // 世界の幕開けだ・・・！
    fun beginningWorld() {

        // get the center for the clipping circle
        val cx = binding.frameNextStage.measuredWidth / 2
        val cy = binding.frameNextStage.measuredHeight / 2

        // get the initial radius for the clipping circle
        val initialRadius = binding.frameNextStage.width

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(binding.frameNextStage, cx, cy, initialRadius.toFloat(), 0f)

        binding.frameNextStage.visibility = View.VISIBLE

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.frameNextStage.visibility = View.INVISIBLE
            }
        })

        // start the animation
        anim.start()
    }

}
