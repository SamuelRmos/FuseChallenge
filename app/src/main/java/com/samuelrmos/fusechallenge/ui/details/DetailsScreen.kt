package com.samuelrmos.fusechallenge.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberAsyncImagePainter
import com.samuelrmos.fusechallenge.data.League
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.data.Players
import com.samuelrmos.fusechallenge.data.Serie
import com.samuelrmos.fusechallenge.navigation.Actions
import com.samuelrmos.fusechallenge.ui.custom.CardOpponentsContent
import com.samuelrmos.fusechallenge.ui.theme.BackgroundColor
import com.samuelrmos.fusechallenge.ui.theme.CardBackgroundColor
import com.samuelrmos.fusechallenge.ui.theme.ColorSecondaryText
import com.samuelrmos.fusechallenge.ui.theme.ColorText
import com.samuelrmos.fusechallenge.ui.theme.FusechallengeTheme
import com.samuelrmos.fusechallenge.ui.theme.ImageBackgroundColor
import com.samuelrmos.fusechallenge.ui.theme.robotoBold
import com.samuelrmos.fusechallenge.ui.theme.robotoMedium
import com.samuelrmos.fusechallenge.ui.theme.robotoRegular
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    matchItem: MatchItem,
    actions: Actions,
    backStackEntry: NavBackStackEntry
) {
    val viewModel: DetailsScreenViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)
    val requestState by viewModel.stateTeamsResponse.collectAsState()

    AnimatedVisibility(
        visible = true,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(rememberScrollState(), Orientation.Vertical)
                .background(BackgroundColor)
        ) {
            if (matchItem.firstOpponent != null && matchItem.secondOpponent != null) {
                HeaderScreen(matchItem.league, matchItem.serie, actions)
                Spacer(modifier = Modifier.height(20.dp))
                CardOpponentsContent(
                    firstOpponent = matchItem.firstOpponent,
                    secondOpponent = matchItem.secondOpponent
                )
                Spacer(modifier = Modifier.height(20.dp))
                MatchDateTime(matchItem)
                Spacer(modifier = Modifier.height(20.dp))
            }
            if (requestState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(Modifier.testTag("loading"))
                }
            }
            AnimatedVisibility(
                visible = true,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ParallelPlayersLists(
                    requestState.playersFirstTeams,
                    requestState.playersSecondTeams
                )
            }
        }
    }
}

@Composable
fun MatchDateTime(matchItem: MatchItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 18.sp,
            text = matchItem.beginAt.orEmpty(),
            color = ColorText,
            fontFamily = robotoBold
        )
    }
}

@Composable
fun HeaderScreen(league: League, serie: Serie, actions: Actions) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            Modifier
                .fillMaxWidth(0.1F)
                .padding(start = 10.dp)
                .clickable { actions.goToListScreen() },
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "backIcon",
                tint = ColorText
            )
        }

        Column(
            Modifier
                .fillMaxWidth(0.9F)
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                fontSize = 18.sp,
                text = "${league.name.orEmpty()} ${serie.name.orEmpty()}",
                color = ColorText,
                fontFamily = robotoMedium
            )
        }
    }
}

@Composable
fun ParallelPlayersLists(
    playersFirstTeams: List<Players?>?,
    playersSecondTeams: List<Players?>?
) {
    val listStateLeft = rememberLazyListState()
    val listStateRight = rememberLazyListState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyColumn(
            state = listStateLeft,
            modifier = Modifier.fillMaxWidth(0.5F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            playersFirstTeams?.forEach {
                item {
                    CardLeftListPlayers(it)
                }
            }
        }
        LazyColumn(
            state = listStateRight,
            modifier = Modifier.fillMaxWidth(0.9F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            playersSecondTeams?.forEach {
                item {
                    CardRightListPlayers(it)
                }
            }
        }
    }
}

@Composable
fun CardLeftListPlayers(player: Players?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 10.dp)
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = CardBackgroundColor,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 10.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        text = player?.nickName.orEmpty(),
                        color = ColorText,
                        fontFamily = robotoBold
                    )
                    Text(
                        fontSize = 12.sp,
                        text = "${player?.firstName.orEmpty()} ${player?.lastName.orEmpty()}",
                        color = ColorSecondaryText,
                        fontFamily = robotoRegular
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp)
                        .padding(end = 5.dp, bottom = 5.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 10.dp,
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                ) {
                    Image(
                        modifier = Modifier.background(ImageBackgroundColor),
                        painter = rememberAsyncImagePainter(player?.imageUrl.orEmpty()),
                        contentDescription = player?.nickName.orEmpty(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun CardRightListPlayers(player: Players?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 10.dp)
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = CardBackgroundColor,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp)
                        .padding(start = 5.dp, bottom = 5.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 10.dp,
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                ) {
                    Image(
                        modifier = Modifier.background(ImageBackgroundColor),
                        painter = rememberAsyncImagePainter(player?.imageUrl.orEmpty()),
                        contentDescription = player?.nickName.orEmpty(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = player?.nickName.orEmpty(),
                        color = ColorText,
                        fontFamily = robotoBold
                    )
                    Text(
                        fontSize = 12.sp,
                        text = "${player?.firstName.orEmpty()} ${player?.lastName.orEmpty()}",
                        color = ColorSecondaryText,
                        fontFamily = robotoRegular
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CardsPlayersPreview() {
    FusechallengeTheme {
        Column {
            CardRightListPlayers(player = players)
            CardLeftListPlayers(player = players)
        }
    }
}