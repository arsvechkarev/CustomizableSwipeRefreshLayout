package com.arsvechkarev.swipetorefreshtest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.swipetorefreshtest.lib.MySwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<View>(R.id.textView)
        view.setOnClickListener {
            (view.parent as MySwipeRefreshLayout).isRefreshing = false
        }
    }
}