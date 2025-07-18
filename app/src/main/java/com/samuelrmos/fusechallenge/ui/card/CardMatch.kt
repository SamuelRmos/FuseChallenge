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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelrmos.fusechallenge.data.League
import com.samuelrmos.fusechallenge.data.Opponents
import com.samuelrmos.fusechallenge.data.Serie

@Composable
fun CardMatch(
    matchTime: String?,
    matchStatus: String?,
    opponent1: Opponents,
    opponent2: Opponents,
    league: League,
    serie: Serie,
    onclick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 20.dp, end = 20.dp),
        onClick = onclick,
        colors = CardDefaults.cardColors(
            containerColor = onBackgroundColor
        )
    ) {
        buildHeadContent(matchTime = matchTime, matchStatus = matchStatus)
        buildOpponentsContent(opponent1 = opponent1, opponent2 = opponent2)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        buildLeagueContent(league = league, serie = serie)
    }
}

@Composable
private fun buildHeadContent(
    matchTime: String?,
    matchStatus: String?,
) {

    val isMatchStarted = matchStatus.equals(stringResource(id = R.string.match_status_running_flag))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomStart = 20.dp))
                .background(
                    color = if (isMatchStarted) {
                        redMatch
                    } else {
                        grayMatch
                    }
                )
                .padding(top = 5.dp, end = 10.dp, bottom = 5.dp, start = 10.dp)
        ) {
            Text(
                fontSize = 12.sp,
                text = if (isMatchStarted) {
                    stringResource(id = R.string.running_now_match)
                } else {
                    matchTime?.let { it } ?: ""
                },
                color = colorText
            )
        }
    }
}

@Composable
fun buildOpponentsContent(opponent1: Opponents, opponent2: Opponents) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
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
            OpponentLogoComponent(opponent1)
            Spacer(Modifier.width(10.dp))
            Text(
                fontSize = 12.sp,
                text = stringResource(id = R.string.match_card_vs_text),
                color = colorText
            )
            Spacer(Modifier.width(10.dp))
            OpponentLogoComponent(opponent2)
        }
    }
}

@Composable
fun buildLeagueContent(league: League?, serie: Serie?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        LeagueDetailsLogoComponent(league = league, serie = serie)
    }
}