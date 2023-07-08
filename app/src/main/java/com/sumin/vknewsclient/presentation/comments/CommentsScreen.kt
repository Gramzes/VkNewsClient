package com.sumin.vknewsclient.presentation.comments

import android.app.Application
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.application.component
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.Comment
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.presentation.ViewModelFactory
import com.sumin.vknewsclient.ui.theme.VKBlue

@Composable
fun CommentsScreen(feedPost: FeedPost, onBackPressed: () -> Unit){
    val component = getComponent()
        .getCommentScreenComponentFactory()
        .create(feedPost)
    val viewModelFactory = component.getViewModelFactory()
    val viewModel: CommentsViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.screenState.collectAsState(CommentsScreenState.Initial)
    CommentScreenContent(
        state = state,
        onBackPressed = onBackPressed
    )
}

@Composable
fun CommentScreenContent(
    state: State<CommentsScreenState>,
    onBackPressed: () -> Unit
    ){
    val currentState = state.value
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.comments)) },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
    }) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (currentState){
                is CommentsScreenState.Comments -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(currentState.comments) { comment ->
                            PostCommentItem(comment = comment)
                        }
                    }
                }
                CommentsScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VKBlue)
                    }
                }
                CommentsScreenState.Initial -> {

                }
            }
        }
    }
}

@Composable
fun PostCommentItem(comment: Comment){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        AsyncImage(
            model = comment.authorAvatarUrl,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(
                text = comment.authorName,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = comment.commentText,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = comment.publicationDate,
                color = MaterialTheme.colors.onSecondary,
                fontSize = 14.sp
            )
        }
    }
}