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
import com.samuelrmos.fusechallenge.ui.theme.ColorText

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
                painter = rememberAsyncImagePainter(opponent.opponentDescriptions.imageUrl),
                contentDescription = opponent.opponentDescriptions.name,
                contentScale = ContentScale.Inside
            )
        }
        Text(
            fontSize = 12.sp,
            text = opponent.opponentDescriptions.name,
            color = ColorText
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
                painter = rememberAsyncImagePainter(league.imageUrl),
                contentDescription = league.name,
                contentScale = ContentScale.Inside
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            fontSize = 10.5.sp,
            text = "${league.name} ${serie.name}",
            color = ColorText
        )
    }
}