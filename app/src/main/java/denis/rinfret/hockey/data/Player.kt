package denis.rinfret.hockey.data

import denis.rinfret.hockey.R

class Player(
    val id: Int,
    val number: Int,
    val name: String,
    val image_resource: Int = R.drawable.nhl_logo
)

fun getSamplePlayers() = listOf(
    Player(1, 66, "Mario Lemieux", R.drawable.mario_lemieux),
    Player(2, 99, "Wayne Gretzky"),
    Player(3, 10, "Guy Lafleur"),
    Player(4, 9, "Maurice Richard"),
    Player(5, 66, "Mario Lemieux", R.drawable.mario_lemieux),
    Player(6, 66, "Mario Lemieux", R.drawable.mario_lemieux),
    Player(7, 66, "Mario Lemieux", R.drawable.mario_lemieux),
    Player(8, 66, "Mario Lemieux", R.drawable.mario_lemieux),
    Player(9, 66, "Mario Lemieux", R.drawable.mario_lemieux)
)