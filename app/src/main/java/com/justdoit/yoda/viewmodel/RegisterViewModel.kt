package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.api.FirebaseAuthService
import com.justdoit.yoda.repository.UserRepository
import com.justdoit.yoda.ui.RegisterActivity
import com.justdoit.yoda.utils.FirebaseAuthUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterViewModel(app: Application): AndroidViewModel(app) {
    private val firebaseAuthService = FirebaseAuthService()
    private val firebaseAuthUtil = FirebaseAuthUtil()
    private val userRepository = UserRepository.getInstance()

    val statusText: ObservableField<String> = ObservableField()

    init {
        this.statusText.set("認証準備中...")
    }

    fun doSmsAuth(activity: RegisterActivity) {
        val phoneNumber = firebaseAuthUtil.getPhoneNumber() ?: run {
            activity.requestPermission()
            return
        }
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) { this@RegisterViewModel.statusText.set("SMSを待機中...") }
            val sms = this@RegisterViewModel.firebaseAuthUtil.getSMSCode(activity, phoneNumber) ?: run {
                GlobalScope.launch(Dispatchers.Main) {
                    this@RegisterViewModel.statusText.set("認証失敗")
                    activity.showToast(activity.getString(R.string.toast_firebase_auth_failed))
                }
                return@launch
            }
            this@RegisterViewModel.loginAndFetchUserInfo(sms.verificationID, sms.smsCode) {
                activity.finishLogin()
            }
        }

    }

    private fun loginAndFetchUserInfo(verificationID: String, smsCode: String, callback: (() -> Unit)) = GlobalScope.launch {
        GlobalScope.launch(Dispatchers.Main) { this@RegisterViewModel.statusText.set("ログイン中...") }
        val idToken = this@RegisterViewModel.firebaseAuthService.getIdToken(verificationID, smsCode) ?: return@launch
        val res = this@RegisterViewModel.userRepository.loginByFirebase(idToken).await()?.takeUnless { it.hasError }?.body ?: return@launch
        SessionManager.instance.login(res.user)
        callback.invoke()
    }
}