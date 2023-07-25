package com.sumin.vknewsclient.presentation.profile.components

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.domain.model.User

@Composable
fun ProfileCard(userState: State<User?>, onMoreDetailsClick: () -> Unit){
    Card(shape = RoundedCornerShape(15.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = userState.value
            if (user != null) {
                AsyncImage(
                    model = user.photoUrl,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onMoreDetailsClick),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    if (!user.status.isNullOrEmpty()){
                        Text(text = user.status)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        user.city?.let {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.place_outline),
                                contentDescription = null,
                                tint = MaterialTheme.colors.onSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = it,
                                color = MaterialTheme.colors.onSecondary
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.info_outline),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Подробнее",
                            color = MaterialTheme.colors.onSecondary
                        )
                    }
                }
            }
        }
    }
}