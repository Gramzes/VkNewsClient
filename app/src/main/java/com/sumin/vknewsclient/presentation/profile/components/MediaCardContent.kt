package com.sumin.vknewsclient.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
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
import coil.compose.AsyncImage
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.ui.theme.VKBlue
import kotlin.math.min


@Composable
fun MediaCardContent(profilePhotosState: State<List<Photo>>) {
    Column(
        modifier = Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.picture_outline),
                    contentDescription = null,
                    tint = VKBlue
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Фото",
                    color = VKBlue
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                val photos = profilePhotosState.value
                val rows = ((min(photos.size, 6) - 1) / 3) + 1
                repeat(rows) { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        repeat(3) { column ->
                            val curRow = row + 1
                            val curCol = column + 1
                            val photo = photos.getOrNull(3 * (curRow - 1) + curCol - 1)
                            if (photo!= null) {
                                AsyncImage(
                                    model = photo.mediumSizeUrl,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                )
                            } else{
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}