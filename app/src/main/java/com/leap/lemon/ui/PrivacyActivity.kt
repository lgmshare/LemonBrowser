package com.leap.lemon.ui

import android.os.Bundle
import com.leap.idea.databinding.ActivityPrivacyBinding

class PrivacyActivity : SimpleActivity() {

    private lateinit var binding: ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}