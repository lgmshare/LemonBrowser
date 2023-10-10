package com.leap.lemon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leap.idea.databinding.ActivityFullscreenBinding
import com.leap.lemon.App
import com.leap.lemon.utils.FirebaseEventUtil
import com.leap.lemon.utils.UserConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class FullscreenActivity : SimpleActivity() {

    private lateinit var binding: ActivityFullscreenBinding

    private var progressTask: Timer? = null
    private var progressActive: Boolean = false
    private var progressNum: Int = 1
    private var job: Job? = null

    private var isSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        if (UserConfig.first_launch) {
            UserConfig.first_launch = false
            UserConfig.country = Locale.getDefault().country
            FirebaseEventUtil.event("lemon_first")
        }

        if (isSplash) {
            FirebaseEventUtil.event("lemon_cold")
        } else {
            FirebaseEventUtil.event("lemon_hot")
        }

        FirebaseEventUtil.userProperty(UserConfig.country)

        binding.progressBar.progress = 0
        progressNum = 1
        progressActive = true
        startProgress()

        job = lifecycleScope.launch {
            delay(3000)
            progressNum = 5
            delay(2000)
            if (isSplash) {
                startActivity(Intent(this@FullscreenActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        progressTask?.cancel()
    }

    private fun startProgress() {
        progressTask = Timer();
        progressTask?.schedule(object : TimerTask() {
            override fun run() {
                val progress = binding.progressBar.progress
                if (progressActive && progress < 100) {
                    binding.progressBar.progress = progress + progressNum
                }
            }
        }, 140, 140)
    }
}