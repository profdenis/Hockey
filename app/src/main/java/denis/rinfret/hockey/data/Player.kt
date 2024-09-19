package denis.rinfret.hockey.data

import denis.rinfret.hockey.R


data class HockeyPlayer(
    val id: Int,
    val name: String,
    val number: Int,
    val position: String,
    val team: String,
    val age: Int,
    val height: Float, // en mètres
    val weight: Int, // en kilogrammes
    val nationality: String,
    val gamesPlayed: Int,
    val goals: Int,
    val assists: Int,
    val penaltyMinutes: Int,
    val photoResources: List<Int> = listOf(R.drawable.nhl_logo)// Liste des ID de ressources pour les photos
) {
    val totalPoints: Int
        get() = goals + assists

    fun isVeteran(): Boolean = gamesPlayed > 1000
}

fun getPlayers(name: String? = null, number: Int? = null): List<HockeyPlayer> =
    getSamplePlayers()
        .filter { player ->
            (name == null || player.name.lowercase().contains(name.lowercase())) &&
                    (number == null || player.number == number)
        }


fun getSamplePlayers(): List<HockeyPlayer> {
    return listOf(
        HockeyPlayer(
            id = 1,
            name = "Wayne Gretzky",
            number = 99,
            position = "Center",
            team = "Edmonton Oilers",
            age = 60,
            height = 1.83f,
            weight = 84,
            nationality = "Canadian",
            gamesPlayed = 1487,
            goals = 894,
            assists = 1963,
            penaltyMinutes = 577,
            photoResources = listOf(
                R.drawable.gretzky_photo
            )
        ),
        HockeyPlayer(
            id = 2,
            name = "Sidney Crosby",
            number = 87,
            position = "Center",
            team = "Pittsburgh Penguins",
            age = 34,
            height = 1.80f,
            weight = 91,
            nationality = "Canadian",
            gamesPlayed = 1039,
            goals = 486,
            assists = 839,
            penaltyMinutes = 505,
            photoResources = listOf(
                R.drawable.crosby_photo,
                R.drawable.crosby_photo2,
                R.drawable.crosby_photo3
            )
        ),
        HockeyPlayer(
            id = 3,
            name = "Connor McDavid",
            number = 97,
            position = "Center",
            team = "Edmonton Oilers",
            age = 26,
            height = 1.83f,
            weight = 91,
            nationality = "Canadian",
            gamesPlayed = 600,
            goals = 241,
            assists = 440,
            penaltyMinutes = 183
        ),
        HockeyPlayer(
            id = 4,
            name = "Nathan MacKinnon",
            number = 29,
            position = "Center",
            team = "Colorado Avalanche",
            age = 28,
            height = 1.83f,
            weight = 93,
            nationality = "Canadian",
            gamesPlayed = 600,
            goals = 251,
            assists = 408,
            penaltyMinutes = 102
        ),
        HockeyPlayer(
            id = 5,
            name = "Auston Matthews",
            number = 34,
            position = "Center",
            team = "Toronto Maple Leafs",
            age = 26,
            height = 1.88f,
            weight = 100,
            nationality = "American",
            gamesPlayed = 400,
            goals = 200,
            assists = 205,
            penaltyMinutes = 150
        ),
        HockeyPlayer(
            id = 6,
            name = "Leon Draisaitl",
            number = 29,
            position = "Center",
            team = "Edmonton Oilers",
            age = 28,
            height = 1.92f,
            weight = 110,
            nationality = "German",
            gamesPlayed = 500,
            goals = 220,
            assists = 300,
            penaltyMinutes = 200
        ),
        HockeyPlayer(
            id = 7,
            name = "David Pastrnak",
            number = 88,
            position = "Right Wing",
            team = "Boston Bruins",
            age = 27,
            height = 1.80f,
            weight = 79,
            nationality = "Czech",
            gamesPlayed = 500,
            goals = 210,
            assists = 200,
            penaltyMinutes = 130
        ),
        HockeyPlayer(
            id = 8,
            name = "Nikita Kucherov",
            number = 86,
            position = "Right Wing",
            team = "Tampa Bay Lightning",
            age = 30,
            height = 1.82f,
            weight = 90,
            nationality = "Russian",
            gamesPlayed = 600,
            goals = 220,
            assists = 300,
            penaltyMinutes = 100
        ),
        HockeyPlayer(
            id = 9,
            name = "Artemi Panarin",
            number = 10,
            position = "Left Wing",
            team = "New York Rangers",
            age = 31,
            height = 1.80f,
            weight = 77,
            nationality = "Russian",
            gamesPlayed = 500,
            goals = 180,
            assists = 320,
            penaltyMinutes = 80
        ),

        HockeyPlayer(
            id = 10,
            name = "Cale Makar",
            number = 8,
            position = "Defenseman",
            team = "Colorado Avalanche",
            age = 24,
            height = 1.80f,
            weight = 84,
            nationality = "Canadian",
            gamesPlayed = 250,
            goals = 50,
            assists = 150,
            penaltyMinutes = 60
        ),
        HockeyPlayer(
            id = 11,
            name = "Andrei Vasilevskiy",
            number = 88,
            position = "Goalie",
            team = "Tampa Bay Lightning",
            age = 30,
            height = 1.93f,
            weight = 98,
            nationality = "Russian",
            gamesPlayed = 500,
            goals = 0,
            assists = 0,
            penaltyMinutes = 40
        ),
        HockeyPlayer(
            id = 12,
            name = "Anthony Mantha",
            number = 39,
            position = "Right Wing",
            team = "Washington Capitals",
            age = 29,
            height = 1.96f,
            weight = 107,
            nationality = "Canadian",
            gamesPlayed = 450,
            goals = 130,
            assists = 130,
            penaltyMinutes = 200
        ),
        HockeyPlayer(
            id = 13,
            name = "Pierre-Olivier Joseph",
            number = 73,
            position = "Defenseman",
            team = "Pittsburgh Penguins",
            age = 24,
            height = 1.88f,
            weight = 84,
            nationality = "Canadian",
            gamesPlayed = 150,
            goals = 10,
            assists = 40,
            penaltyMinutes = 60
        ),
        HockeyPlayer(
            id = 14,
            name = "Mathieu Joseph",
            number = 21,
            position = "Right Wing",
            team = "Ottawa Senators",
            age = 26,
            height = 1.83f,
            weight = 84,
            nationality = "Canadian",
            gamesPlayed = 300,
            goals = 50,
            assists = 70,
            penaltyMinutes = 100
        ),
        HockeyPlayer(
            id = 15,
            name = "Yanni Gourde",
            number = 37,
            position = "Center",
            team = "Seattle Kraken",
            age = 32,
            height = 1.75f,
            weight = 77,
            nationality = "Canadian",
            gamesPlayed = 500,
            goals = 120,
            assists = 180,
            penaltyMinutes = 250
        ),
        HockeyPlayer(
            id = 16,
            name = "David Savard",
            number = 58,
            position = "Defenseman",
            team = "Montreal Canadiens",
            age = 33,
            height = 1.88f,
            weight = 100,
            nationality = "Canadian",
            gamesPlayed = 700,
            goals = 50,
            assists = 150,
            penaltyMinutes = 400
        )
    )
}