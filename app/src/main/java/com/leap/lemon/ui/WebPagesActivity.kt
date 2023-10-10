package com.leap.lemon.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.leap.idea.databinding.ActivityWebPagesBinding
import com.leap.lemon.WebPagesManager
import com.leap.lemon.models.WebTab
import com.leap.lemon.utils.FirebaseEventUtil

class WebPagesActivity : SimpleActivity() {

    private lateinit var binding: ActivityWebPagesBinding
    private lateinit var adapter: WebPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseEventUtil.event("lemon_showTab")
        binding.run {
            btnAdd.setOnClickListener {
                FirebaseEventUtil.newTabEvent("tab")
                WebPagesManager.addNewWeb()
                finish()
            }

            btnBack.setOnClickListener {
                finish()
            }

        }

        adapter = WebPageAdapter(this, { item, _ ->
            WebPagesManager.changeWeb(item)
            finish()
        }, { item, _ ->
            remoteWeb(item)
        })

        binding.tabsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.tabsRecyclerView.adapter = adapter

        updateData()
    }

    private fun remoteWeb(link: WebTab) {
        WebPagesManager.remove(link)
        updateData()
    }

    private fun updateData() {
        val list = WebPagesManager.webPageLists.sortedByDescending { it.createTime }
        adapter.dataList.clear()
        adapter.dataList.addAll(list)
        adapter.notifyDataSetChanged()
    }
}