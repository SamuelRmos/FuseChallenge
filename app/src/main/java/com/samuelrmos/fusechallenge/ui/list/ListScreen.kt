package com.samuelrmos.fusechallenge.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.samuelrmos.fusechallenge.navigation.Actions

@Composable
fun ListMatchScreen(
    modifier: Modifier,
    actions: Actions,
    viewModel: ListMatchesViewModel
) {
    AnimatedVisibility(
        visible = true,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        SwipeRefresh(
            state = stateSwipeToRefresh,
            onRefresh = { onSwipeAction() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                buildCardsContent(
                    matchesListState = matchesListState,
                    onCardClicked = onCardClicked,
                    onLoadMoreItems = onLoadMoreItems
                )
            }
        }
    }
}