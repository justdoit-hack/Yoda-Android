package com.justdoit.yoda.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.justdoit.yoda.R
import com.justdoit.yoda.utils.OnBackKeyHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        // Navigationを使っている画面のRootに相当するNavHostFragmentの取得
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        // fragmentsにはNavHostFragmentにaddされた現在表示中の画面(Fragment)と、UIを持たないFragmentNavigator#StateFragmentだけが存在します
        val target = navHost.childFragmentManager.findFragmentById(R.id.fragment_container)

        if (target is OnBackKeyHandler) {
            if (target.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

}
