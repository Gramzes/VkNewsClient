package com.sumin.vknewsclient.presentation.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.sumin.vknewsclient.domain.model.User
import kotlin.math.min

@Composable
fun FriendsCard(userState: State<User?>, friendsState: State<List<User>>) {
    Card(
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            userState.value?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${it.friendsCount} друзей",
                    color = MaterialTheme.colors.onBackground
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                val friends = friendsState.value
                repeat(min(3, friends.size)){
                    AsyncImage(
                        model = friends[it].photoUrl,
                        modifier = Modifier
                            .zIndex(-it.toFloat())
                            .size(30.dp)
                            .border(1.dp, MaterialTheme.colors.background, CircleShape)
                            .padding(1.dp)
                            .clip(CircleShape),
                        contentDescription = null
                    )
                }
            }
        }
    }
}