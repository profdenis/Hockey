package denis.rinfret.hockey

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import denis.rinfret.hockey.data.HockeyPlayer
import denis.rinfret.hockey.ui.theme.HockeyTheme

class PlayerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = intent.getParcelableExtra("player", HockeyPlayer::class.java)

        setContent {
            HockeyTheme(darkTheme = true) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight / 2,
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(onClick = { finish() },
                                        modifier = Modifier.align(Alignment.BottomStart)) {
                                        Icon(Icons.Default.Done, "done")
                                    }
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            })
                    }
                ) { innerPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        if (player != null)
                            HockeyPlayerCard(
                                player = player,
                                expandable = true,
                                clickable = false
                            )
                        else {
                            Text(text = stringResource(R.string.player_not_found))
                        }
                    }
                }

            }
        }
    }
}

