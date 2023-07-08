package com.sumin.vknewsclient.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sumin.vknewsclient.R
import com.sumin.vknewsclient.application.getComponent
import com.sumin.vknewsclient.domain.model.Photo
import com.sumin.vknewsclient.domain.model.User
import com.sumin.vknewsclient.ui.theme.VKBlue
import com.sumin.vknewsclient.ui.theme.VkNewsClientTheme
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun ProfileScreen(){
    val factory = getComponent().getViewModelFactory()
    val viewModel: ProfileViewModel = viewModel(factory = factory)
    val userState = viewModel.profileState.collectAsState(initial = null)
    val profilePhotosState = viewModel.profilePhotosState.collectAsState()
    val friendsState = viewModel.friendsState.collectAsState(initial = listOf())

    SheetLayout(userState, profilePhotosState, friendsState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetLayout(
    userState: State<User?>,
    profilePhotosState: State<List<Photo>>,
    friendsState: State<List<User>>
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
                ProfileContent(userState, profilePhotosState, friendsState){
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
private fun AboutElement(text: String, iconResId: Int){
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(19.dp),
            imageVector = ImageVector.vectorResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colors.onSecondary
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = MaterialTheme.colors.onSecondary)
    }
}

@Composable
private fun ProfileContent(
    userState: State<User?>,
    profilePhotosState: State<List<Photo>>,
    friendsState: State<List<User>>,
    onMoreDetailsClick: () -> Unit
){
    VkNewsClientTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.secondary)
        ) {
            Column {
                ProfileCard(userState, onMoreDetailsClick)
                Spacer(modifier = Modifier.height(10.dp))
                FriendsCard(userState, friendsState)
                Spacer(modifier = Modifier.height(10.dp))
                MediaCard(profilePhotosState)
                Spacer(modifier = Modifier.height(10.dp))
                WallCard()
            }
        }
    }
}

@Composable
private fun ProfileCard(userState: State<User?>, onMoreDetailsClick: () -> Unit){
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

@Composable
fun MediaCard(profilePhotosState: State<List<Photo>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp)
    ) {

        Column() {
            MediaCardContent(profilePhotosState)

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
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WallCard(){
    VkNewsClientTheme {
        LazyColumn() {
            item {
                Card(
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = "63 записи",
                            color = MaterialTheme.colors.onSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider()
                    }
                }
            }
        }
    }
}
