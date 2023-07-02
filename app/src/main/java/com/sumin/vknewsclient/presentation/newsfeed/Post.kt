package com.sumin.vknewsclient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sumin.vknewsclient.domain.model.FeedPost
import com.sumin.vknewsclient.domain.model.StatisticItem
import com.sumin.vknewsclient.domain.model.Statistics
import com.sumin.vknewsclient.ui.theme.LikeRed

@Composable
fun Post(feedPost: FeedPost,
         onLikeClickListener: (FeedPost, StatisticItem) -> Unit,
         onCommentClickListener: (FeedPost, StatisticItem) -> Unit,
         modifier: Modifier = Modifier){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(7.dp))
            PostContent(feedPost)
            Spacer(modifier = Modifier.height(10.dp))
            PostFooter(
                feedPost.statistics,
                { item -> onLikeClickListener(feedPost, item) },
                { item -> onCommentClickListener(feedPost, item) }
            )
        }
    }
}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = feedPost.avatarUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = "")
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colors.onPrimary
            )
            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colors.onSecondary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            tint = MaterialTheme.colors.onSecondary,
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = ""
        )
    }
}

@Composable
private fun PostContent(feedPost: FeedPost) {
    if (feedPost.contentText.isNotEmpty()) {
        Text(
            text = feedPost.contentText,
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
    AsyncImage(
        model = feedPost.contentImageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(3.dp)),
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun PostFooter(
    statistics: Statistics,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Details(
                iconId = R.drawable.ic_views_count,
                count = statistics.views.count,
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Details(
                iconId = R.drawable.ic_share,
                count = statistics.shares.count
            )
            Details(
                iconId = R.drawable.ic_comment,
                count = statistics.comments.count,
                onClickListener = {
                    onCommentClickListener(statistics.comments)
                }
            )
            Details(
                iconId = if (statistics.likes.isLiked) R.drawable.ic_like_set else R.drawable.ic_like,
                count = statistics.likes.count,
                onClickListener = {
                    onLikeClickListener(statistics.likes)
                },
                tint = if (statistics.likes.isLiked) LikeRed else MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
private fun Details(
    iconId: Int,
    count: Int,
    onClickListener: (() -> Unit)? = null,
    tint: Color = MaterialTheme.colors.onSecondary
){
    val modifier = if (onClickListener != null){
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClickListener() }
    } else{
        Modifier
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = iconId),
                contentDescription = "",
                tint = tint
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = formatStatisticCount(count),
                color = MaterialTheme.colors.onSecondary,
                fontSize = 15.sp
            )
        }
    }
}

fun formatStatisticCount(count: Int): String{
    return if (count >= 100_000){
        String.format("%dK", count/1000)
    } else if (count >= 1000){
        String.format("%.1fK", count/1000f)
    } else{
        count.toString()
    }
}

