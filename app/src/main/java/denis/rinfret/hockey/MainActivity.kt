package denis.rinfret.hockey

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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
fun HockeyPlayerCard(
    player: HockeyPlayer,
    expandable: Boolean = false,
    clickable: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(expandable) }

    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                if (clickable) {
                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("player", player)
                    context.startActivity(intent)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // la mise en page a besoin d'être améliorée
            if (expanded) {

                if (isLandscape) {
                    Row {
                        PlayerBasicData(player)
                        PlayerDetails(player)
                    }
                    HorizontalImageCarousel(photoResources = player.photoResources)
                } else {
                    PlayerBasicData(player)
                    PlayerDetails(player)
                    VerticalImageCarousel(photoResources = player.photoResources)
                }
            } else {
                PlayerBasicData(player)
            }
        }
    }
}

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
            PlayerDataRow(R.string.number, player.number.toString())
            PlayerDataRow(R.string.team, player.team)
            PlayerDataRow(R.string.position, player.position)
        }
    }
}

@Composable
private fun PlayerDetails(player: HockeyPlayer) {
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            PlayerDataRow(R.string.age, stringResource(R.string.years, player.age))
            PlayerDataRow(R.string.height, "${player.height} m")
            PlayerDataRow(R.string.weight, "${player.weight} kg")
            PlayerDataRow(R.string.nationality, player.nationality)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            PlayerDataRow(R.string.games_played, player.gamesPlayed.toString())
            PlayerDataRow(R.string.goals, player.goals.toString())
            PlayerDataRow(R.string.assists, player.assists.toString())
            PlayerDataRow(R.string.penalty_minutes, player.penaltyMinutes.toString())
            PlayerDataRow(R.string.total_points, player.totalPoints.toString())
        }
    }
}

@Composable
private fun PlayerDataRow(labelResource: Int, data: String) {
    Row {
        Text(
            text = stringResource(labelResource),
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = data,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun VerticalImageCarousel(photoResources: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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

@Composable
fun HorizontalImageCarousel(photoResources: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyRow(
                modifier = Modifier.align(Alignment.Center),
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