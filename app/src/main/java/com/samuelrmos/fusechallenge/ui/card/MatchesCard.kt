package com.samuelrmos.fusechallenge.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelrmos.fusechallenge.data.Game
import com.samuelrmos.fusechallenge.data.League
import com.samuelrmos.fusechallenge.data.Opponents
import com.samuelrmos.fusechallenge.data.Serie
import com.samuelrmos.fusechallenge.ui.custom.LeagueCustomComponent
import com.samuelrmos.fusechallenge.ui.custom.TeamsImageComponent
import com.samuelrmos.fusechallenge.ui.list.ListMatchesViewModel
import com.samuelrmos.fusechallenge.ui.theme.CardBackgroundColor
import com.samuelrmos.fusechallenge.ui.theme.ColorText
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchesCard(
    matchTime: String?,
    firstOpponent: Opponents,
    secondOpponent: Opponents,
    league: League,
    serie: Serie,
    game: Game,
    onclick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(212.dp)
            .padding(start = 20.dp, end = 20.dp),
        onClick = onclick,
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        )
    ) {
        CardHeadContent(matchTime = matchTime, matchStatus = game.status)
        CardOpponentsContent(firstOpponent = firstOpponent, secondOpponent = secondOpponent)
        HorizontalDivider(modifier =Modifier.fillMaxWidth())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            LeagueCustomComponent(league = league, serie = serie)
        }
    }
}

@Composable
private fun CardHeadContent(
    matchTime: String?,
    matchStatus: String,
    viewModel: ListMatchesViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomStart = 20.dp))
                .background(
                    color = viewModel.getComponentColor(matchStatus)
                )
                .padding(top = 5.dp, end = 10.dp, bottom = 5.dp, start = 10.dp)
        ) {
            Text(
                fontSize = 12.sp,
                text = viewModel.checkGameStatusAndReturnText(matchStatus, matchTime.orEmpty()),
                color = ColorText
            )
        }
    }
}

@Composable
fun CardOpponentsContent(firstOpponent: Opponents, secondOpponent: Opponents) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 50.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamsImageComponent(firstOpponent)
            Spacer(Modifier.width(10.dp))
            Text(
                fontSize = 12.sp,
                text = "VS",
                color = ColorText
            )
            Spacer(Modifier.width(10.dp))
            TeamsImageComponent(secondOpponent)
        }
    }
}