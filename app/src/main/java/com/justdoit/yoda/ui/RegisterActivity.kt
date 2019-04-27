package com.justdoit.yoda.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.databinding.ActivityRegisterBinding
import com.justdoit.yoda.viewmodel.RegisterViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_register)

        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
        binding.viewModel = this.viewModel

        this.doSmsAuth()
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 0)
    }

    private fun doSmsAuth() = this.viewModel.doSmsAuth(this)

    fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun finishLogin() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            task.takeIf { it.isSuccessful }?.let {
                val registerToken = it.result?.token ?: return@let
                val authToken = SessionManager.instance.authToken ?: return@let
                GlobalScope.launch {
                    val res = this@RegisterActivity.viewModel.userRepository.registerNotificationToken(authToken, registerToken).await() ?: return@launch
                    if (res.hasError) {
                        Log.e(TAG, res.error.toString())
                    }
                }
            }
        }
        this.startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (grantResults[0]) {
            PackageManager.PERMISSION_GRANTED -> this.doSmsAuth()
            PackageManager.PERMISSION_DENIED -> {
                this.showToast(this.getText(R.string.toast_permission_denied))
            }
        }
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}
