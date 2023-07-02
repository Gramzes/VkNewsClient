package com.sumin.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import com.sumin.vknewsclient.domain.model.FeedPost

sealed class Screen(val route: String){

    object Favorite: Screen(ROUTE_FAVORITE)
    object Profile: Screen(ROUTE_PROFILE)
    object Home: Screen(ROUTE_HOME)
    object Comments: Screen(ROUTE_COMMENTS) {
        const val ROUTE_COMMENTS_BASE = "comments"
        const val KEY_FEED_POST = "feed_post"

        fun getRouteWithArgs(feedPost: FeedPost): String{
            val json = Gson().toJson(feedPost)
            return "$ROUTE_COMMENTS_BASE/${Uri.encode(json)}"
        }
    }
    object NewsFeed: Screen(ROUTE_NEWS_FEED)

    companion object{
        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "${Comments.ROUTE_COMMENTS_BASE}/{${Comments.KEY_FEED_POST}}"
        const val ROUTE_NEWS_FEED = "newsfeed"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_PROFILE = "profile"
    }
}
