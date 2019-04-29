package com.justdoit.yoda.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.Yoda
import com.justdoit.yoda.databinding.FragmentSendBinding
import com.justdoit.yoda.viewmodel.SendViewModel

class SendFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SendViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSendBinding>(inflater, R.layout.fragment_send, container, false)
        binding.viewModel = this.viewModel
        SessionManager.instance.user?.let {
            binding.myInAppPhoneNoText.text = "# ${it.inAppPhoneNo}"
        }
        this.arguments?.let {
            val reply = SendFragmentArgs.fromBundle(it).replyMessage ?: return@let
            val replyInAppPhoneNo = reply.fromUser?.inAppPhoneNo ?: return@let
            binding.inputInAppPhoneNoContainer.visibility = View.GONE
            binding.replyMessageContainer.visibility = View.VISIBLE
            binding.replyInAppPhoneNoText.text = replyInAppPhoneNo
            binding.replyMessageText.text = reply.originalBody
        }
        binding.messageText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener  when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val toInAppPhoneNo = (binding.textInputSendPhoneNo.text ?: binding.replyInAppPhoneNoText.text) ?: return@setOnEditorActionListener false
                    this.viewModel.postMessage(toInAppPhoneNo.toString(), v.text.toString())
                    true
                }
                else -> false
            }
        }
        this.viewModel.finishPostEvent.observe(this, Observer {
            val result = it["success"]?.toBoolean() ?: return@Observer
            when (result) {
                true -> this.backToMainFragment(binding.root)
                false -> this.sendFailed(it["errorMessage"])
            }
        })

        return binding.root
    }

    private fun backToMainFragment(view: View) {
        val imm = Yoda.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        Toast.makeText(this.context, R.string.toast_send_message_success, Toast.LENGTH_LONG).show()
        Navigation.findNavController(view).popBackStack()
    }

    private fun sendFailed(message: String?) {
        val errorMessage = message ?: this.getString(R.string.toast_send_message_failed)
        Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show()
    }
}
