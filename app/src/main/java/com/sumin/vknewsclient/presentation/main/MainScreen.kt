package com.sumin.vknewsclient.presentation.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sumin.vknewsclient.navigation.AppNavGraph
import com.sumin.vknewsclient.navigation.NavigationState
import com.sumin.vknewsclient.navigation.rememberNavState
import com.sumin.vknewsclient.presentation.comments.CommentsScreen
import com.sumin.vknewsclient.presentation.newsfeed.NewsFeedScreen

@Composable
fun MainScreen(){
    val navState = rememberNavState()
    Scaffold(bottomBar = { BottomBar(navState) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colors.background)
        ) {
            AppNavGraph(
                navHostController = navState.navController,
                newsFeedScreenContent = {
                    NewsFeedScreen(
                        onCommentsPressed = {
                            navState.navigateToComments(it)
                        }
                    ) },
                commentScreenContent = {
                    CommentsScreen(
                        feedPost = it,
                        onBackPressed = {
                            navState.navController.popBackStack()
                        }
                    ) },
                favoriteScreenContent = { TextCounter(name = "Favorite") },
                profileScreenContent = { TextCounter(name = "Profile") }
            )
        }
    }
}

@Composable
fun BottomBar(navState: NavigationState){
    val backStack by navState.navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination

    val categories = listOf(NavigationItem.Home, NavigationItem.Favorite, NavigationItem.Profile)

    BottomNavigation(){
        Log.d("TEST", "BottomNav")
        categories.forEachIndexed { index, item ->
            val selected = currentDestination?.hierarchy?.any{
                it.route == item.screen.route
            } ?: false
            BottomNavigationItem(
                selected = selected,
                onClick = { if (!selected) navState.navigateTo(item.screen.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = "") },
                label = { Text(text = stringResource(id = item.titleResId)) },
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onSecondary
            )
        }
    }
}
@Composable
fun TextCounter(name: String){
    var count by rememberSaveable {
        mutableStateOf(0)
    }
    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name: $count",
        color = Color.Black)
}