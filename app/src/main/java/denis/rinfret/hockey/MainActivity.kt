package denis.rinfret.hockey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import denis.rinfret.hockey.data.HockeyPlayer
import denis.rinfret.hockey.data.Player
import denis.rinfret.hockey.data.getPlayers
import denis.rinfret.hockey.data.getSamplePlayers
import denis.rinfret.hockey.ui.theme.HockeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HockeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlayerListWithSearch(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: HockeyPlayer, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, colorResource(R.color.purple_500)),
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.purple_100))
        ) {
            PlayerCardFirstRow(
                player = player,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            PlayerCardSecondRow(
                player = player,
                modifier = Modifier.Companion.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun PlayerCardFirstRow(player: HockeyPlayer, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(10.dp)

    ) {
        Image(
            painter = painterResource(
                if (player.photoResources.isEmpty())
                    R.drawable.nhl_logo
                else
                    player.photoResources[0]
            ),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(100.dp)
                .clip(RoundedCornerShape(25.dp))
        )
    }
}

@Composable
private fun PlayerCardSecondRow(player: HockeyPlayer, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(text = player.number.toString())
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = player.name)
    }
}

@Composable
fun PlayerList(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(getSamplePlayers()) {
            PlayerCard(player = it)
        }
    }
}

@Composable
fun PlayerListWithSearch(modifier: Modifier = Modifier) {
    var nameSearch by rememberSaveable { mutableStateOf("") }
    var numberSearch: Int? by rememberSaveable { mutableStateOf(null) }
    Column(modifier = modifier) {
        Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
            TextField(
                value = nameSearch,
                label = { Text(text = "Nom") },
                onValueChange = { nameSearch = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
            TextField(
                value = (numberSearch ?: "").toString(),
                label = { Text(text = "Numéro") },
                onValueChange = { numberSearch = it.toIntOrNull() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(getPlayers(nameSearch, numberSearch)) {
                PlayerCard(player = it)
            }
        }
    }
}

//@Preview
@Composable
fun PlayerCardPreview() {
    HockeyTheme {
        PlayerCard(getSamplePlayers()[0])
    }
}

//@Preview(showBackground = true)
@Composable
fun PlayerListPreview() {
    HockeyTheme {
        PlayerList()
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    HockeyTheme {
        PlayerListWithSearch()
    }
}