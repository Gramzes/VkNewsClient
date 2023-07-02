package com.sumin.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sumin.vknewsclient.domain.model.FeedPost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    commentScreenContent: @Composable (FeedPost) -> Unit,
    favoriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit
){
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route){

        homeNavGraph(
            newsFeedScreenContent,
            commentScreenContent
        )
        composable(Screen.Favorite.route){
            favoriteScreenContent()
        }
        composable(Screen.Profile.route){
            profileScreenContent()
        }
    }
}