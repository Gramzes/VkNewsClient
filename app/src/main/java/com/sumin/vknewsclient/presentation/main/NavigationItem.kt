package com.sumin.vknewsclient.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
    ){

    object Home: NavigationItem(Screen.Home,
        R.string.nav_item_home, Icons.Outlined.Home)

    object Favorite: NavigationItem(Screen.Favorite,
        R.string.nav_item_favorite, Icons.Outlined.Favorite)

    object Profile: NavigationItem(Screen.Profile,
        R.string.nav_item_profile, Icons.Outlined.Person)
}
