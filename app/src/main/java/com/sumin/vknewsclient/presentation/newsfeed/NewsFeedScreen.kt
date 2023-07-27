package com.sumin.vknewsclient.presentation.newsfeed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.presentation.components.Post
import com.sumin.vknewsclient.ui.theme.VKBlue

@Composable
fun NewsFeedScreen(onCommentsPressed: (FeedPost) -> Unit){
    val component = getComponent()
    val viewModel: NewsFeedViewModel = viewModel(factory = component.getViewModelFactory())
    val state = viewModel.newsFeedState.collectAsState(initial = NewsFeedScreenState.Initial)
    MainScreenContent(
        state = state,
        viewModel = viewModel,
        onCommentsPressed = onCommentsPressed
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    state: State<NewsFeedScreenState>,
    viewModel: NewsFeedViewModel,
    onCommentsPressed: (FeedPost) -> Unit
){
    val currentState = state.value
    if (currentState is NewsFeedScreenState.Posts) {
        val posts = currentState.postsState
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colors.secondary),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(posts, key = { it.id }) {
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.deletePost(it)
                }
                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {}
                ) {
                    Post(
                        it,
                        { feedPost, _ -> viewModel.changeLikeStatus(feedPost)},
                        { feedPost, _ -> onCommentsPressed(feedPost) }
                    )
                }
            }
            item {
                if (currentState.isNextLoading){
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(
                            color = VKBlue
                        )
                    }
                } else if (!currentState.endOfData){
                    SideEffect {
                        viewModel.loadNextNewsFeed()
                    }
                }
            }
        }
    } else if (currentState is NewsFeedScreenState.Loading){
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