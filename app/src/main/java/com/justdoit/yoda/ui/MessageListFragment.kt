package com.justdoit.yoda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.justdoit.yoda.adapter.MessageListAdapter
import com.justdoit.yoda.databinding.FragmentListBinding
import com.justdoit.yoda.viewmodel.MessageListViewModel


class MessageListFragment : Fragment() {

    private val viewModel: MessageListViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(MessageListViewModel::class.java)
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
//        viewModel.items?.observe(this, Observer { list ->
//            adapter.submitList(list)
//        })

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.messageList.layoutManager = linearLayoutManager
        binding.messageList.adapter = adapter

//        binding.addBtn.setOnClickListener {
//            sendSMS(APIClient.getE164PhoneNumber(activity!!))
//        }

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

//    private fun sendSMS(phoneNumber: String) = GlobalScope.launch {
//        val util = SystemUtil()
//        val smsEntity = util.getSMSCode(activity as Activity, phoneNumber)
//        if (smsEntity == null) Log.d("SMS_ERROR", "sms error !")
//        smsEntity?.let {
//            register(smsEntity.verificationID, smsEntity.smsCode)
//        }
//    }

//    fun register(verificationID: String, smsCode: String) = GlobalScope.launch {
//        val firebaseApi = FirebaseApi()
//        val token = firebaseApi.getIdToken(verificationID, smsCode)
//        Log.d("ID_TOKEN", token)
//
//        val userRepo = UserRepository.getInstance()
//        val userRes = userRepo.loginByFirebase(token).await() ?: return@launch
//        userRes.takeUnless { it.hasError }?.let {
//            val userResponse = it.body ?: return@let
//            Log.d("ID_TOKEN", userResponse.user.authToken)
//        } ?: run {
//            Log.e("TOKEN_REGISTER_ERROR", userRes.error.toString())
//        }
//    }

}
