package denis.rinfret.hockey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import denis.rinfret.hockey.data.HockeyPlayer
import denis.rinfret.hockey.data.filterPlayers
import denis.rinfret.hockey.data.getSamplePlayers
import denis.rinfret.hockey.data.readPlayersFromFile
import denis.rinfret.hockey.data.saveToFile
import denis.rinfret.hockey.ui.theme.HockeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HockeyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HockeyApp() {
    // ici, je force l'utilisation du le thème sombre ou clair, ce qu'on ne devrait pas faire normalement
    HockeyTheme(darkTheme = true) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight / 2,
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    })
            }
        ) { innerPadding ->
            PlayerListWithSearch(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun PlayerListWithSearch(modifier: Modifier = Modifier) {
    var nameSearch by rememberSaveable { mutableStateOf("") }
    var numberSearch: Int? by rememberSaveable { mutableStateOf(null) }

    Column(modifier = modifier) {
        SearchTextFields(
            nameSearch = nameSearch,
            onNameChange = { nameSearch = it },
            numberSearch = numberSearch,
            onNumberChange =
            { numberSearch = if (it.isEmpty()) null else it.toIntOrNull() ?: numberSearch }
        )

        val context = LocalContext.current
        var playerList = context.readPlayersFromFile(stringResource(R.string.players_json))
        if (playerList.isEmpty()) {
            playerList = getSamplePlayers()
            context.saveToFile(playerList, stringResource(R.string.players_json))
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 400.dp)) {
            items(filterPlayers(playerList, nameSearch, numberSearch)) { player ->
                HockeyPlayerCard(player = player)
            }
        }
    }
}

@Composable
private fun SearchTextFields(
    nameSearch: String,
    onNameChange: (String) -> Unit,
    numberSearch: Int?,
    onNumberChange: (String) -> Unit
) {
    Row(modifier = Modifier.padding(top = 10.dp)) {
        TextField(
            value = nameSearch,
            label = { Text(text = stringResource(R.string.name_label)) },
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth(0.67f)
                .padding(start = 20.dp)
        )
        TextField(
            value = (numberSearch ?: "").toString(),
            label = { Text(text = stringResource(R.string.number_label)) },
            onValueChange = onNumberChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 20.dp)
        )
    }
}


@Composable
fun HockeyPlayerCard(player: HockeyPlayer, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PlayerBasicData(player)

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                // Informations supplémentaires du joueur


                PlayerDetails(player)
                ImageCarousel(photoResources = player.photoResources)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerBasicData(player: HockeyPlayer) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Photo du joueur
        player.photoResources.firstOrNull()?.let { photoResource ->
            Image(
                painter = painterResource(id = photoResource),
                contentDescription = stringResource(R.string.first_photo, player.name),
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Informations du joueur
        Column {
            Row {
                if (player.isVeteran()) {
                    Icon(
                        Icons.Outlined.Star,
                        stringResource(R.string.veteran),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.num_ro, player.number),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.team, player.team),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.position, player.position),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PlayerDetails(player: HockeyPlayer) {
    Row {
        Column {
            Text(
                text = stringResource(R.string.age, player.age),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.height, player.height),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.weight, player.weight),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.nationality, player.nationality),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {

            Text(
                text = stringResource(R.string.games_played, player.gamesPlayed),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.goals, player.goals),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.passes, player.assists),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.penalty_minutes, player.penaltyMinutes),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.total_points, player.totalPoints),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ImageCarousel(photoResources: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(photoResources) { index, photoResource ->
                    Image(
                        painter = painterResource(id = photoResource),
                        contentDescription = stringResource(R.string.player_photo, index + 1),
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayerCardPreview() {
    HockeyTheme {
        HockeyPlayerCard(getSamplePlayers()[0])
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    HockeyApp()
}