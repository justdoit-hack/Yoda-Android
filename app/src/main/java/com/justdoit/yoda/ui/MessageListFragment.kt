package com.justdoit.yoda.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.justdoit.yoda.APIClient
import com.justdoit.yoda.adapter.MessageListAdapter
import com.justdoit.yoda.databinding.FragmentListBinding
import com.justdoit.yoda.viewmodel.MessageListViewModel
import java.util.concurrent.TimeUnit


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

        binding.addBtn.setOnClickListener {
            sendSMS(APIClient.getE164PhoneNumber(activity!!))
        }

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

    private fun sendSMS(phoneNumber: String) {
        var mSMSVerificationID = ""
        val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // 後でつかいます
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                register(mSMSVerificationID, p0?.smsCode!!)
            }

            // 後でつかいます
            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(context, p0?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                mSMSVerificationID = p0.toString()
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                mSMSVerificationID = p0.toString()
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // E.164フォーマットである必要があります。このフォーマットは[+][国コード][エリアコード]になります。例えば080-1234-5678が電話番号なら、E.164フォーマットで+818012345678です。
            10, // 10秒でタイムアウトする
            TimeUnit.SECONDS,
            activity as Activity,
            mCallbacks
        )
    }

    fun register(verificationID: String, smsCode: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = PhoneAuthProvider.getCredential(verificationID, smsCode)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.let {
                    it.getIdToken(true).addOnCompleteListener { tokenResult ->
                        if (tokenResult.isSuccessful) {
                            val idToken = tokenResult.result?.token
                            Toast.makeText(context, idToken, Toast.LENGTH_LONG).show()
                            Log.w("ID_TOKEN", idToken, task.exception)
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                }
            } else {
                Log.w("Phone", "signInWithPhoneAuthCredential:failure", task.exception)
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
