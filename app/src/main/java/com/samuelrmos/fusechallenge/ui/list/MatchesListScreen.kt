package com.samuelrmos.fusechallenge.ui.list

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelrmos.fusechallenge.R
import com.samuelrmos.fusechallenge.navigation.Actions
import com.samuelrmos.fusechallenge.navigation.Screens.DetailScreen
import com.samuelrmos.fusechallenge.ui.card.MatchesCard
import com.samuelrmos.fusechallenge.ui.theme.BackgroundColor
import com.samuelrmos.fusechallenge.ui.theme.ColorText
import com.samuelrmos.fusechallenge.ui.theme.robotoRegular

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MatchesListScreen(
    modifier: Modifier = Modifier,
    actions: Actions,
    viewModel: ListMatchesViewModel
) {
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundColor.toArgb()
        }
    }
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )

    val requestState by viewModel.stateMatchesResponse.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp, bottom = 15.dp, top = 20.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 35.sp,
            text = stringResource(R.string.matches),
            color = ColorText,
            fontFamily = robotoRegular
        )

        with(requestState) {
            when {
                isLoading -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(BackgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(Modifier.testTag("loading"))
                    }
                }

                response != null -> {
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(pullRefreshState)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(23.dp)
                            ) {
                                response.forEach { matches ->
                                    item {
                                        if (matches?.firstOpponent != null
                                            && matches.secondOpponent != null
                                        ) {
                                            MatchesCard(
                                                matches.beginAt,
                                                matches.firstOpponent,
                                                matches.secondOpponent,
                                                matches.league,
                                                matches.serie,
                                                matches.game
                                            ) {
                                                actions.goToDetailScreen(
                                                    Pair(
                                                        DetailScreen.route,
                                                        matches
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            PullRefreshIndicator(
                                modifier = Modifier.align(Alignment.TopCenter),
                                state = pullRefreshState,
                                refreshing = requestState.isLoading
                            )
                        }
                    }
                }

                errorMessage.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(all = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage.orEmpty(),
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }
    }
}