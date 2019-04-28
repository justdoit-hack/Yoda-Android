package com.justdoit.yoda.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
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
    lateinit var animCircle: Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
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

        var pointTouchX = 0f
        var pointTouchY = 0f
        binding.frameScreen.setOnTouchListener { view, motionEvent ->
            if (SessionManager.instance.isLogin()) {
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    pointTouchX = motionEvent.x
                    pointTouchY = motionEvent.y
                    binding.frameNextStage.visibility = View.INVISIBLE
                } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (getDistance(pointTouchX, pointTouchY, motionEvent.x, motionEvent.y) > 300) {
                        binding.frameNextStage.visibility = View.VISIBLE
                        evolutionNextStage(pointTouchX, pointTouchY)
                        //animCircle.cancel()
                    }
                }
            }
            return@setOnTouchListener true
        }

        binding.frameBody.setOnClickListener {
            //FIXME クリックキャッチアップのため泣く泣く実装
        }

        viewModel.doSmsAuth(this)

        return binding.root
    }

    fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x2 - x1) * (x2 - x1)
        val y = (y2 - y1) * (y2 - y1)
        return Math.sqrt((x + y).toDouble()).toFloat()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    // コイツは進化する。次のステージへ・・・！
    fun evolutionNextStage(pointX: Float, pointY: Float) {
        // get the final radius for the clipping circle
        val finalRadius = Math.max(binding.frameNextStage.width, binding.frameNextStage.height)

        // create the animator for this view (the start radius is zero)
        animCircle = ViewAnimationUtils.createCircularReveal(
            binding.frameNextStage,
            pointX.toInt(),
            pointY.toInt(),
            0f,
            finalRadius.toFloat()
        )

        animCircle.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                Navigation.findNavController(binding.frameNextStage).navigate(R.id.action_pocketbell_to_list)
            }
        })
        animCircle.start()
    }
}
