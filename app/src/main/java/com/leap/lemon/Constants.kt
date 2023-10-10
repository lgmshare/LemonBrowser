package com.leap.lemon

import com.leap.idea.R
import com.leap.lemon.models.WebLink

const val LEMON_DEBUG = true

 val LEMON_DEFAULT_LINK = mutableListOf<WebLink>().apply {
    add(WebLink("facebook", "Facebook", "https://www.facebook.com", R.mipmap.tab_link_11))
    add(WebLink("google", "google", "https://www.google.com", R.mipmap.tab_link_18))
    add(WebLink("youtube", "youtube", "https://www.youtube.com", R.mipmap.tab_link_12))
    add(WebLink("twitter", "twitter", "https://www.twitter.com", R.mipmap.tab_link_13))
    add(WebLink("instagram", "instagram", "https://www.instagram.com", R.mipmap.tab_link_14))
    add(WebLink("amazon", "amazon", "https://www.amazon.com", R.mipmap.tab_link_15))
    add(WebLink("tiktok", "tiktok", "https://www.tiktok.com", R.mipmap.tab_link_16))
    add(WebLink("yahoo", "yahoo", "www.yahoo.com", R.mipmap.tab_link_17))
}