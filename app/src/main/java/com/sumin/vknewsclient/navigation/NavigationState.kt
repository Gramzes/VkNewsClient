package com.sumin.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sumin.vknewsclient.domain.model.FeedPost

class NavigationState(
    val navController: NavHostController
) {

    fun navigateTo(route: String){
        navController.navigate(route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToComments(feedPost: FeedPost){
        navController.navigate(Screen.Comments.getRouteWithArgs(feedPost))
    }
}

@Composable
fun rememberNavState(
    navController: NavHostController = rememberNavController()
): NavigationState{
    return remember {
        NavigationState(navController)
    }
}