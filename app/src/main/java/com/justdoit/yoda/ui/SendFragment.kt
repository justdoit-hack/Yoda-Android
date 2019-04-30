package com.justdoit.yoda.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.justdoit.yoda.GeneralSystem
import com.justdoit.yoda.R.*
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.databinding.FragmentSendBinding
import com.justdoit.yoda.entity.SourceTypeEnum
import com.justdoit.yoda.utils.OnBackKeyHandler
import com.justdoit.yoda.viewmodel.SendViewModel

class SendFragment : Fragment(), OnBackKeyHandler {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SendViewModel::class.java)
    }

    lateinit var binding: FragmentSendBinding
    lateinit var replyInAppPhoneNo: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            layout.fragment_send,
            container,
            false
        )
        binding.viewModel = this.viewModel
        SessionManager.instance.user?.let {
            binding.myInAppPhoneNoText.text = "# ${it.inAppPhoneNo}"
        }

        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        binding.toolbar.title = "New Message"
        binding.textInputSendPhoneNo.requestFocus()

        this.arguments?.let {
            val reply = SendFragmentArgs.fromBundle(it).replyMessage ?: return@let
            replyInAppPhoneNo = reply.fromUser?.inAppPhoneNo ?: return@let

            binding.toolbar.title = "Reply Message"
            binding.messageText.requestFocus()

            binding.inputInAppPhoneNoContainer.visibility = View.GONE
            binding.replyMessage.visibility = View.VISIBLE
            if (reply.sourceType == SourceTypeEnum.API) {
                // スマホアプリ
                binding.fromUserIcon.setImageResource(drawable.ic_user_api)
            } else if (reply.sourceType == SourceTypeEnum.ASTERISK) {
                // 固定電話
                binding.fromUserIcon.setImageResource(drawable.ic_user_phone)
            } else if (reply.sourceType == SourceTypeEnum.ANONYMOUS) {
                // 非通知
                binding.fromUserIcon.setImageResource(drawable.ic_user_none)
            }
            binding.fromInAppPhoneNoText.text = "# $replyInAppPhoneNo"
            binding.textDate.text = GeneralSystem.parseFromISO8601(reply.updatedAt)
            binding.replyMessageText.text = reply.originalBody

            binding.translateBtn.setOnClickListener {
                if (binding.replyMessageText.text == reply.originalBody) {
                    binding.frameMessage.setBackgroundResource(drawable.frame_light)
                    binding.replyMessageText.setTextColor(Color.parseColor("#C6B399"))
                    binding.replyMessageText.text = reply.parsed
                } else {
                    binding.frameMessage.setBackgroundResource(drawable.frame_black)
                    binding.replyMessageText.setTextColor(Color.parseColor("#333333"))
                    binding.replyMessageText.text = reply.originalBody
                }
            }
        }
        binding.messageText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val toInAppPhoneNo = (
                            binding.textInputSendPhoneNo.text?.takeIf { it.isNotEmpty() }
                                ?: replyInAppPhoneNo.takeIf { it.isNotEmpty() }
                            ) ?: return@setOnEditorActionListener false
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
        binding.messageText.clearFocus()
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
        Toast.makeText(this.context, string.toast_send_message_success, Toast.LENGTH_LONG).show()
        Navigation.findNavController(view).popBackStack()
    }

    private fun sendFailed(message: String?) {
        val errorMessage = message ?: this.getString(string.toast_send_message_failed)
        Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed(): Boolean {
        Navigation.findNavController(binding.toolbar).popBackStack()
        return true
    }
}
