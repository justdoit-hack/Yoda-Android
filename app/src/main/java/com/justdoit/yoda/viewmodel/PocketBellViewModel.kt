package com.justdoit.yoda.viewmodel

import android.R
import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.view.animation.AnimationUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.api.FirebaseAuthService
import com.justdoit.yoda.repository.UserRepository
import com.justdoit.yoda.ui.PocketBellFragment
import com.justdoit.yoda.utils.FirebaseAuthUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PocketBellViewModel(app: Application) : AndroidViewModel(app) {
    private val firebaseAuthService by lazy { FirebaseAuthService() }
    private val firebaseAuthUtil by lazy { FirebaseAuthUtil() }
    val userRepository by lazy { UserRepository.getInstance() }
    val statusDisplay: ObservableField<DisplayInfo> = ObservableField()
    val statusUserPhoneNumber: ObservableField<String> = ObservableField()
    private var page = -1

    var flagLoadingFinish = false

    val loadingColor = Color.parseColor("#8899F2")
    val displayLoadsingSMS = DisplayInfo("SMS認証中...", loadingColor)
    val displayFailed = DisplayInfo("認証失敗\n再認証してください", loadingColor)
    val displayLogin = DisplayInfo("認証成功\nログイン中...", loadingColor)
    val displayDeniedPermission = DisplayInfo("権限を得られませんでした\n再認証してください", loadingColor)

    val displays = listOf(
        DisplayInfo("0", Color.parseColor("#EFEDE4")),
        DisplayInfo("1", Color.parseColor("#EFEDE4"))
    )

    val displayUniqueLoadings = listOf(
        DisplayInfo("いろんな処理中...", loadingColor),
        DisplayInfo("いろんな処理中...\nupdate 平成...", loadingColor),
        DisplayInfo("update 平成...\n平成楽しかった...", loadingColor),
        DisplayInfo("平成楽しかった...\nポケベル良かったよね...", loadingColor),
        DisplayInfo("ポケベル良かったよね...\nごめんなさい、仕事します", loadingColor),
        DisplayInfo("ごめんなさい、仕事します\nいろんな処理中...", loadingColor)
    )

    init {
        val statusInit = DisplayInfo("ポケテル♪", Color.parseColor("#C6B399"))
        statusDisplay.set(statusInit)
        statusUserPhoneNumber.set("")
        page = -1
        flagLoadingFinish = false
    }

    fun startPocketBell() {
        statusUserPhoneNumber.set("#${SessionManager.instance.user?.inAppPhoneNo}")
        page = 0
        changePage(page)
    }

    fun clickLeftBtn() {
        if (page > -1) {
            page--
            if (page < 0) {
                page = displays.size - 1
            }
            changePage(page)
        }
    }

    fun clickRightBtn() {
        if (page > -1) {
            page++
            if (page >= displays.size) {
                page = 0
            }
            changePage(page)
        }
    }

    fun changePage(page: Int) {
        statusDisplay.set(displays[page])
    }

    fun deniedPermission() {
        statusDisplay.set(displayDeniedPermission)
    }

    // 最初にこいつを呼んでスタート
    fun doSmsAuth(fragment: PocketBellFragment) {
        // もう登録されてたらUserデータ取得してポケベル起動
        if (SessionManager.instance.isRegistered()) {
            GlobalScope.launch {
                val res =
                    this@PocketBellViewModel.userRepository.fetchUser(SessionManager.instance.authToken!!).await()?.takeUnless { it.hasError }?.body
                        ?: return@launch
                SessionManager.instance.login(res.user)
                startPocketBell()
            }
            return
        }

        val phoneNumber = firebaseAuthUtil.getPhoneNumber() ?: run {
            fragment.requestPermission()
            return
        }
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) {
                this@PocketBellViewModel.statusDisplay.set(displayLoadsingSMS)
                this@PocketBellViewModel.startUniqueLoading()
            }
            val sms = this@PocketBellViewModel.firebaseAuthUtil.getSMSCode(fragment.activity as Activity, phoneNumber)
                ?: run {
                    GlobalScope.launch(Dispatchers.Main) {
                        flagLoadingFinish = true
                        this@PocketBellViewModel.statusDisplay.set(displayFailed)
                    }
                    return@launch
                }
            flagLoadingFinish = true
            this@PocketBellViewModel.loginAndFetchUserInfo(sms.verificationID, sms.smsCode) {
                fragment.finishLogin()
            }
        }

    }

    private fun loginAndFetchUserInfo(verificationID: String, smsCode: String, callback: (() -> Unit)) =
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) { this@PocketBellViewModel.statusDisplay.set(displayLogin) }
            val idToken =
                this@PocketBellViewModel.firebaseAuthService.getIdToken(verificationID, smsCode) ?: return@launch
            val res =
                this@PocketBellViewModel.userRepository.loginByFirebase(idToken).await()?.takeUnless { it.hasError }?.body
                    ?: return@launch
            SessionManager.instance.login(res.user)
            callback.invoke()
        }

    fun startUniqueLoading() = GlobalScope.launch {
        for (i in 0 until 100) {
            delay(200)
            if (flagLoadingFinish) {
                break
            }
            uniqueLoad(i)
        }
        flagLoadingFinish = false
    }

    fun uniqueLoad(index: Int) {
        statusDisplay.set(displayUniqueLoadings[index % displayUniqueLoadings.size])
    }

    data class DisplayInfo(
        val text: String,
        val colorBG: Int
    )
}
