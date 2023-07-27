package com.sumin.vknewsclient.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.presentation.components.Post
import com.sumin.vknewsclient.presentation.profile.components.AboutElement
import com.sumin.vknewsclient.presentation.profile.components.FriendsCard
import com.sumin.vknewsclient.presentation.profile.components.MediaCard
import com.sumin.vknewsclient.presentation.profile.components.ProfileCard
import com.sumin.vknewsclient.presentation.profile.state.PostsState
import com.sumin.vknewsclient.presentation.profile.state.ProfileScreenState
import com.sumin.vknewsclient.presentation.profile.state.ProfileState
import com.sumin.vknewsclient.ui.theme.VKBlue
import com.sumin.vknewsclient.ui.theme.VkNewsClientTheme
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(){
    val factory = getComponent().getViewModelFactory()
    val viewModel: ProfileViewModel = viewModel(factory = factory)

    val profileScreenState = viewModel.profileState.collectAsState()
    val postsState = viewModel.postsState.collectAsState()

    when (val profileState = profileScreenState.value){
        ProfileScreenState.Initial -> {}
        ProfileScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = VKBlue
                )
            }
        }
        is ProfileScreenState.ProfileInfo -> {
            SheetLayout(profileState, postsState,
                onLoadNextPosts = viewModel::loadNextPosts,
                onChangeLikeStatus = viewModel::changeLikeStatus
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetLayout(
    profileInfoState: ProfileScreenState.ProfileInfo,
    postsState: State<PostsState>,
    onLoadNextPosts: () -> Unit,
    onChangeLikeStatus: (FeedPost) -> Unit
){
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()
    VkNewsClientTheme() {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SheetContent(profileInfoState.profileState)
            },
            content = {
                ProfileContent(profileInfoState, postsState,
                    onMoreDetailsClick = {
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    },
                    onLoadNextPosts = onLoadNextPosts,
                    onChangeLikeStatus = onChangeLikeStatus
                )
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
private fun SheetContent(
    profileState: ProfileState
) {
    VkNewsClientTheme() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column() {
                Text(
                    text = "Подробнее",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colors.onBackground,
                )
                Spacer(modifier = Modifier.height(10.dp))
                when(profileState){
                    ProfileState.Initial -> {}
                    is ProfileState.Profile -> {
                        val user = profileState.profile
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
}

@Composable
private fun ProfileContent(
    profileInfoState: ProfileScreenState.ProfileInfo,
    postsState: State<PostsState>,
    onMoreDetailsClick: () -> Unit,
    onLoadNextPosts: () -> Unit,
    onChangeLikeStatus: (FeedPost) -> Unit
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
                        ProfileCard(profileInfoState.profileState, onMoreDetailsClick)
                        Spacer(modifier = Modifier.height(10.dp))
                        FriendsCard(profileInfoState.friendsState)
                        Spacer(modifier = Modifier.height(10.dp))
                        MediaCard(profileInfoState.photosState)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                when(val postsInfo = postsState.value){
                    PostsState.Initial -> {}
                    PostsState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator(
                                    color = VKBlue
                                )
                            }
                        }
                    }
                    is PostsState.Posts -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(15.dp)
                                ) {
                                    Text(
                                        text = "${postsInfo.allPostsCount} записи",
                                        color = MaterialTheme.colors.onSecondary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider()
                                }
                            }
                        }

                        items(postsInfo.posts){
                            Post(
                                feedPost = it,
                                onLikeClickListener = {post, _ -> onChangeLikeStatus(post)},
                                onCommentClickListener = {post, _ -> }
                            )
                        }
                        item {
                            if (postsInfo.isNextLoading && !postsInfo.endOfData){
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ){
                                    CircularProgressIndicator(
                                        color = VKBlue
                                    )
                                }
                            } else if (!postsInfo.endOfData){
                                SideEffect {
                                    onLoadNextPosts()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}