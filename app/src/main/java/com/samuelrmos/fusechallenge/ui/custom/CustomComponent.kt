package com.samuelrmos.fusechallenge.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.samuelrmos.fusechallenge.data.League
import com.samuelrmos.fusechallenge.data.Opponents
import com.samuelrmos.fusechallenge.data.Serie
import com.samuelrmos.fusechallenge.ui.theme.ColorSecondaryText
import com.samuelrmos.fusechallenge.ui.theme.ColorText
import com.samuelrmos.fusechallenge.ui.theme.robotoRegular

@Composable
fun TeamsImageComponent(opponent: Opponents) {
    Column(
        Modifier.height(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            Modifier
                .height(100.dp)
                .width(80.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(opponent.opponentDescriptions.imageUrl.orEmpty()),
                contentDescription = opponent.opponentDescriptions.name.orEmpty(),
                contentScale = ContentScale.Inside
            )
        }
        Text(
            fontSize = 12.sp,
            text = opponent.opponentDescriptions.name.orEmpty(),
            color = ColorText,
            fontFamily = robotoRegular
        )
    }
}

@Composable
fun LeagueCustomComponent(league: League, serie: Serie) {
    Row(
        Modifier
            .height(25.dp)
            .fillMaxWidth()
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.width(20.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(league.imageUrl.orEmpty()),
                contentDescription = league.name.orEmpty(),
                contentScale = ContentScale.Inside
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            fontSize = 10.5.sp,
            text = "${league.name.orEmpty()} ${serie.name.orEmpty()}",
            color = ColorText,
            fontFamily = robotoRegular
        )
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
                color = ColorSecondaryText,
                fontFamily = robotoRegular
            )
            Spacer(Modifier.width(10.dp))
            TeamsImageComponent(secondOpponent)
        }
    }
}