package com.sumin.vknewsclient.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sumin.vknewsclient.presentation.components.Post
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.User
import com.sumin.vknewsclient.ui.theme.VkNewsClientTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import com.sumin.vknewsclient.presentation.profile.components.AboutElement
import com.sumin.vknewsclient.presentation.profile.components.FriendsCard
import com.sumin.vknewsclient.presentation.profile.components.MediaCard
import com.sumin.vknewsclient.presentation.profile.components.ProfileCard

@Composable
fun ProfileScreen(){
    val factory = getComponent().getViewModelFactory()
    val viewModel: ProfileViewModel = viewModel(factory = factory)

    val userState = viewModel.profileState.collectAsState(initial = null)
    val profilePhotosState = viewModel.profilePhotosState.collectAsState(initial = listOf())
    val friendsState = viewModel.friendsState.collectAsState(initial = listOf())
    val postState = viewModel.postsState.collectAsState()
    val postsCount = viewModel.postsCountState.collectAsState()

    SheetLayout(userState, profilePhotosState, friendsState, postState, postsCount)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetLayout(
    userState: State<User?>,
    profilePhotosState: State<List<Photo>>,
    friendsState: State<List<User>>,
    postState: State<List<FeedPost>>,
    postsCount: State<Int?>
){
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()
    VkNewsClientTheme() {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SheetContent(userState)
            },
            content = {
                ProfileContent(userState, profilePhotosState, friendsState, postState, postsCount){
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }
            },
            sheetShape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 10.dp,
                topEnd = 10.dp
            )
        )
    }
}

@Composable
private fun SheetContent(userState: State<User?>) {
    VkNewsClientTheme() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column() {
                 userState.value?.let { user ->
                    Text(
                        text = "Подробнее",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colors.onBackground,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        user.bDate?.let {
                            AboutElement(
                                text = "День рождения: $it",
                                R.drawable.gift_outline_32
                            )
                        }
                        user.city?.let {
                            AboutElement(
                                text = "Город: $it",
                                R.drawable.home_outline
                            )
                        }
                        user.work?.let {
                            AboutElement(
                                text = "Место работы: $it",
                                R.drawable.work_outline
                            )
                        }
                        user.followersCount?.let {
                            val lastDigit = it % 10
                            val lastTwoDigits = it % 100

                            val subscribers = when {
                                lastTwoDigits in 11..19 -> "подписчиков"
                                lastDigit == 1 -> "подписчик"
                                lastDigit in 2..4 -> "подписчика"
                                else -> "подписчиков"
                            }

                            AboutElement(
                                text = "$it $subscribers",
                                R.drawable.rss_feed_outline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    userState: State<User?>,
    profilePhotosState: State<List<Photo>>,
    friendsState: State<List<User>>,
    postState: State<List<FeedPost>>,
    postsCountState: State<Int?>,
    onMoreDetailsClick: () -> Unit
){
    VkNewsClientTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.secondary)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Column {
                        ProfileCard(userState, onMoreDetailsClick)
                        Spacer(modifier = Modifier.height(10.dp))
                        FriendsCard(userState, friendsState)
                        Spacer(modifier = Modifier.height(10.dp))
                        MediaCard(profilePhotosState)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp)
                        ) {
                            val postsCount = postsCountState.value
                            postsCount?.let {
                                Text(
                                    text = "$it записи",
                                    color = MaterialTheme.colors.onSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                        }
                    }
                }
                items(postState.value){
                    Post(
                        feedPost = it,
                        onLikeClickListener = {_, _ ->},
                        onCommentClickListener = {_, _ ->}
                    )
                }
            }
        }
    }
}