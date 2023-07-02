package com.sumin.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.sumin.vknewsclient.domain.model.FeedPost

fun NavGraphBuilder.homeNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentScreenContent: @Composable (FeedPost) -> Unit
){
    navigation(startDestination = Screen.NewsFeed.route, route = Screen.Home.route){
        composable(Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }
        composable(
            Screen.Comments.route,
            arguments = listOf(navArgument(Screen.Comments.KEY_FEED_POST){
                type = NavType.StringType
            })
        ) {
            val json= it.arguments?.getString(Screen.Comments.KEY_FEED_POST) ?: ""
            val post = Gson().fromJson(json, FeedPost::class.java)
            commentScreenContent(post)
        }
    }
}