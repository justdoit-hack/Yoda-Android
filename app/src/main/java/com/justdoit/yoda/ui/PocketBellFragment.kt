package com.justdoit.yoda.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.databinding.FragmentPocketBellBinding
import com.justdoit.yoda.viewmodel.PocketBellViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PocketBellFragment : Fragment() {
    private val viewModel: PocketBellViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(PocketBellViewModel::class.java)
    }

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

        binding.viewModel = viewModel

        binding.btnReload.setOnClickListener {
            viewModel.doSmsAuth(this)
        }

        viewModel.doSmsAuth(this)

        return binding.root
    }

    private fun doSmsAuth() = this.viewModel.doSmsAuth(this)

    fun requestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), 266)
    }

    fun finishLogin() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            task.takeIf { it.isSuccessful }?.let {
                val registerToken = it.result?.token ?: return@let
                val authToken = SessionManager.instance.authToken ?: return@let
                GlobalScope.launch {
                    val res = this@PocketBellFragment.viewModel.userRepository.registerNotificationToken(
                        authToken,
                        registerToken
                    ).await() ?: return@launch
                    if (res.hasError) {
                        Log.e(RegisterActivity.TAG, res.error.toString())
                    }
                }
            }
        }
        this.viewModel.startPocketBell()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 266) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> this.doSmsAuth()
                PackageManager.PERMISSION_DENIED -> {
                    this.viewModel.deniedPermission()
                }
            }
        }
    }
}
