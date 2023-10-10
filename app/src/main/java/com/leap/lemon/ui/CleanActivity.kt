package com.leap.lemon.ui

import android.os.Bundle
import android.webkit.CookieManager
import androidx.lifecycle.lifecycleScope
import com.leap.idea.databinding.ActivityCleanBinding
import com.leap.lemon.WebPagesManager
import com.leap.lemon.exts.toast
import com.leap.lemon.utils.FirebaseEventUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class CleanActivity : SimpleActivity() {

    private lateinit var binding: ActivityCleanBinding

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCleanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = lifecycleScope.launch {
            kotlin.runCatching {
                withTimeoutOrNull(8000) {
                    launch {
                        CookieManager.getInstance().removeAllCookies {
                        }
                        WebPagesManager.cleanAllWeb()
                    }

                    launch {
                        delay(3000)
                        toast("Clean successfully")
                        FirebaseEventUtil.cleanEvent(3)
                    }
                    launch {
                        delay(5000)
                    }
                }
            }.onSuccess {
                FirebaseEventUtil.event("lemon_clean_end")
                finish()
            }.onFailure {
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    override fun onBackPressed() {
    }

}