package com.sumin.vknewsclient.presentation.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumin.vknewsclient.presentation.profile.state.PhotosState
import com.sumin.vknewsclient.ui.theme.VKBlue

@Composable
fun MediaCard(photosState: PhotosState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp)
    ) {

        when (photosState){
            PhotosState.Initial -> TODO()
            PhotosState.NoPhotos -> TODO()
            is PhotosState.Photos -> {
                Column() {
                    MediaCardContent(photosState.photos)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(15.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Показать всё", color = VKBlue)
                        Icon(
                            modifier = Modifier.size(17.dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = VKBlue
                        )
                    }
                }
            }
        }
    }
}