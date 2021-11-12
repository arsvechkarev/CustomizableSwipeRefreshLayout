package com.arsvechkarev.swipetorefreshtest

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.swipetorefresh.CustomizableSwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val swipeToRefresh = findViewById<CustomizableSwipeRefreshLayout>(R.id.swipeRefreshRoot)
        swipeToRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeToRefresh.isRefreshing = false
            }, 3000)
        }
    }
}