package com.leap.lemon.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.leap.idea.R
import com.leap.idea.databinding.ActivityMainBinding
import com.leap.idea.databinding.MainContentBinding
import com.leap.lemon.LEMON_DEFAULT_LINK
import com.leap.lemon.WebPagesManager
import com.leap.lemon.exts.toast
import com.leap.lemon.models.WebTab
import com.leap.lemon.utils.DialogUtils
import com.leap.lemon.utils.FirebaseEventUtil
import com.leap.lemon.utils.KeyboardUtils
import com.leap.lemon.utils.LemonUtils
import com.leap.lemon.widget.MyWebView

class MainActivity : SimpleActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingContent: MainContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingContent = binding.drawerNavContent
        setContentView(binding.root)
        setupNavContent()
        setupNavView()
    }

    private fun setupNavView() {
        val drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        val navHeaderView = binding.drawerNavView.inflateHeaderView(R.layout.main_navigation)
        navHeaderView.findViewById<View>(R.id.item_new).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        navHeaderView.findViewById<View>(R.id.item_share).setOnClickListener {
            FirebaseEventUtil.event("lemon_share")
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            LemonUtils.jumpShare(this@MainActivity, "")
        }
        navHeaderView.findViewById<View>(R.id.item_copy).setOnClickListener {
            FirebaseEventUtil.event("lemon_copy")
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            LemonUtils.copyToClipboard(this@MainActivity, "")
        }
        navHeaderView.findViewById<View>(R.id.item_rate).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            LemonUtils.jumpGooglePlayStore(this@MainActivity)
        }
        navHeaderView.findViewById<View>(R.id.item_terms).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, PrivacyActivity::class.java))
        }
        navHeaderView.findViewById<View>(R.id.item_policy).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, PrivacyActivity::class.java))
        }
    }

    private fun setupNavContent() {
        bindingContent.run {
            root.postDelayed({
                WebPagesManager.updateCurrentWebTabBitmap(binding.root.drawToBitmap())
            }, 300)

            searchView.addTextChangedListener {
                val inputText = bindingContent.searchView.text.toString().trim()
                if (inputText.isEmpty()) {
                    btnSearch.isVisible = true
                    btnDelete.isVisible = false
                } else {
                    btnSearch.isVisible = false
                    btnDelete.isVisible = true
                }
            }

            searchView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    bindingContent.searchView.setText("")
                    stopLoad()
                }
            }

            searchView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(textView: TextView, actionId: Int, p2: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        KeyboardUtils.hideKeyboard(bindingContent.searchView)
                        bindingContent.searchView.clearFocus()
                        val inputText = bindingContent.searchView.text.toString().trim()
                        if (inputText.isEmpty()) {
                            toast("Please enter your search content")
                        } else {
                            FirebaseEventUtil.searchEvent(inputText)
                            startLoad(inputText)
                        }
                        return true
                    }
                    return false
                }
            })

            btnDelete.setOnClickListener {
                bindingContent.searchView.setText("")
            }

            btnBackward.setOnClickListener {
                clickBack()
            }
            btnForward.setOnClickListener {
                clickForward()
            }

            btnClean.setOnClickListener {
                FirebaseEventUtil.event("lemon_clean")
                DialogUtils.showCleanDialog(this@MainActivity) {
                    startActivity(Intent(this@MainActivity, CleanActivity::class.java))
                }
            }
            btnCount.setOnClickListener {
                startActivity(Intent(this@MainActivity, WebPagesActivity::class.java))
            }
            btnSetting.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }

            val adapter = WebLinkAdapter(this@MainActivity) { item, _ ->
                KeyboardUtils.hideKeyboard(searchView)
                searchView.clearFocus()
                startLoad(item.url)
                FirebaseEventUtil.newLinkEvent(item.name)
            }
            adapter.dataList.clear()
            adapter.dataList.addAll(LEMON_DEFAULT_LINK)
            navRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 4)
            navRecyclerView.adapter = adapter
        }

        WebPagesManager.webListener = object : WebPagesManager.WebListener {
            override fun onProgressChanged(progress: Int, webView: MyWebView) {
                if (WebPagesManager.isCurrentWebView(webView) && !webView.isStopped) {
                    bindingContent.progressBar.isVisible = progress < 100
                    bindingContent.progressBar.progress = progress
                    if (progress >= 100) {
                        updateWebViewVisible(true)
                        if (bindingContent.webContainer.childCount >= 1) {
                            val view = bindingContent.webContainer.getChildAt(0)
                            if (view != webView) {
                                bindingContent.webContainer.removeAllViews()
                                (webView.parent as? ViewGroup)?.removeAllViews()
                                bindingContent.webContainer.addView(webView)
                            }
                        } else {
                            bindingContent.webContainer.removeAllViews()
                            (webView.parent as? ViewGroup)?.removeAllViews()
                            bindingContent.webContainer.addView(webView)
                        }
                        updateBottomTools()

                        webView.postDelayed({
                            if (webView.isLaidOut) {
                                WebPagesManager.updateCurrentWebTabBitmap(webView.drawToBitmap())
                            }
                        }, 500)
                    }
                }
            }

            override fun onWebChanged(webTab: WebTab) {
                if (webTab.webView.isLoadingFinish) {
                    updateWebViewVisible(true)
                    bindingContent.progressBar.isVisible = false
                    bindingContent.webContainer.removeAllViews()
                    bindingContent.webContainer.addView(webTab.webView)
                } else {
                    bindingContent.searchView.text = SpannableStringBuilder(webTab.inputText ?: "")
                    bindingContent.progressBar.isVisible = webTab.webView.isLoading
                    updateWebViewVisible(false)
                }

                updateBottomTools()
            }

            override fun clean() {
                bindingContent.progressBar.isVisible = false
                bindingContent.progressBar.progress = 0
                bindingContent.searchView.text = SpannableStringBuilder("")
                updateWebViewVisible(false)
                updateBottomTools()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseEventUtil.event("lemon_show")
    }

    override fun onBackPressed() {
        if (clickBack()) {
            return
        }

        moveTaskToBack(true)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        updatePageBitmap()
    }

    private fun updateWebViewVisible(isVisible: Boolean) {
        bindingContent.webContainer.isVisible = isVisible
        if (!isVisible) {
            bindingContent.progressBar.isVisible = false
            bindingContent.webContainer.removeAllViews()
        }
    }

    private fun updateBottomTools() {
        if (bindingContent.webContainer.isVisible) {
            bindingContent.btnBackward.isEnabled = true
            bindingContent.btnBackward.setImageResource(R.mipmap.ic_previous_enable)
            if (WebPagesManager.currentWebTab.webView.canGoForward()) {
                bindingContent.btnForward.isEnabled = true
                bindingContent.btnForward.setImageResource(R.mipmap.ic_next_enable)
            } else {
                bindingContent.btnForward.isEnabled = false
                bindingContent.btnForward.setImageResource(R.mipmap.ic_next)
            }
        } else {
            WebPagesManager.currentWebTab.webView.clearWebHistory()
            bindingContent.btnBackward.isEnabled = false
            bindingContent.btnForward.isEnabled = false
            bindingContent.webContainer.removeAllViews()
            bindingContent.btnBackward.setImageResource(R.mipmap.ic_previous)
            bindingContent.btnForward.setImageResource(R.mipmap.ic_next)
        }

        bindingContent.tvCount.text = "${WebPagesManager.getTabCount()}"
    }

    private fun updatePageBitmap() {
        val view = bindingContent.webContainer.getChildAt(0)
        if (view != null) {
            WebPagesManager.updateCurrentWebTabBitmap(view.drawToBitmap())
        } else {
            WebPagesManager.updateCurrentWebTabBitmap(binding.root.drawToBitmap())
        }
    }

    private fun startLoad(url: String) {
        FirebaseEventUtil.event("rose_newSearch")
        WebPagesManager.startLoad(url)
    }

    private fun stopLoad() {
        WebPagesManager.stopLoad()
        updateWebViewVisible(false)
    }

    private fun clickBack(): Boolean {
        if (WebPagesManager.currentWebTab.webView.canGoBack()) {
            WebPagesManager.currentWebTab.webView.goBack()
            return true
        } else {
            var backend = false
            if (bindingContent.webContainer.isVisible) {
                backend = true
            }
            WebPagesManager.currentWebTab.webView.stopLoad()
            updateWebViewVisible(false)
            updateBottomTools()
            return backend
        }
    }

    private fun clickForward() {
        if (WebPagesManager.currentWebTab.webView.canGoForward()) {
            WebPagesManager.currentWebTab.webView.goForward()
        } else {
            updateBottomTools()
        }
    }
}