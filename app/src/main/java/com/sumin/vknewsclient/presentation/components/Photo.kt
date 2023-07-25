package com.sumin.vknewsclient.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sumin.vknewsclient.domain.model.Photo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoContent(images: List<Photo>){
    if (!images.isNullOrEmpty()) {
        val averageRatio = images.sumOf { it.photoSizesRatio.toDouble() } / images.size
        var usableRatio: Float? = null
        images.forEach {
            usableRatio = if (usableRatio == null) {
                it.photoSizesRatio
            } else if (Math.abs(it.photoSizesRatio - averageRatio) < Math.abs(usableRatio!! - averageRatio)) {
                it.photoSizesRatio
            } else {
                usableRatio
            }
        }
        if (images.size > 1) {
            HorizontalPager(
                pageCount = images.size,
                pageSpacing = 7.dp,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(usableRatio ?: 1f),
                    contentAlignment = Alignment.Center
                ) {
                    val image = images[it]
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = image.smallSizeUrl,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(3.dp)),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,

                            )
                    }
                    AsyncImage(
                        model = image.mediumSizeUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(3.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        } else {
            AsyncImage(
                model = images.first(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp)),
                contentDescription = "",
                contentScale = ContentScale.FillWidth
            )
        }
    }
}