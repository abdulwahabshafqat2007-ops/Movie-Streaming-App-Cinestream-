package com.example.moviestreamingapp.utils

import com.example.moviestreamingapp.models.Movie

object MovieDataProvider {

    fun getAllMovies(): List<Movie> {
        return listOf(
            Movie(id = 1, title = "The Dark Knight", genre = "Action • Crime",
                rating = 9.0, duration = "2h 32m", year = 2008,
                description = "When the menacing Joker wreaks havoc on Gotham, Batman must confront his deepest fears.",
                posterColor = "#1A237E", watchProgress = 70),
            Movie(id = 2, title = "Interstellar", genre = "Sci-Fi • Drama",
                rating = 8.6, duration = "2h 49m", year = 2014,
                description = "A team of explorers travel through a wormhole in space to ensure humanity's survival.",
                posterColor = "#006064", watchProgress = 30),
            Movie(id = 3, title = "Inception", genre = "Thriller • Sci-Fi",
                rating = 8.8, duration = "2h 28m", year = 2010,
                description = "A skilled thief steals corporate secrets using dream-sharing technology.",
                posterColor = "#4A148C", watchProgress = 85),
            Movie(id = 4, title = "Oppenheimer", genre = "Biography • Drama",
                rating = 8.3, duration = "3h 00m", year = 2023,
                description = "The story of J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                posterColor = "#1A237E", watchProgress = 50),
            Movie(id = 5, title = "Dune: Part Two", genre = "Sci-Fi • Adventure",
                rating = 8.5, duration = "2h 46m", year = 2024,
                description = "Paul Atreides travels to Arrakis to ensure the future of his family and people.",
                posterColor = "#E65100", watchProgress = 15),
            Movie(id = 6, title = "Avengers: Endgame", genre = "Action • Adventure",
                rating = 8.4, duration = "3h 01m", year = 2019,
                description = "The Avengers assemble to reverse Thanos' actions and restore balance.",
                posterColor = "#B71C1C", watchProgress = 100),
            Movie(id = 7, title = "The Shawshank Redemption", genre = "Drama",
                rating = 9.3, duration = "2h 22m", year = 1994,
                description = "Two imprisoned men bond over years, finding redemption through acts of common decency.",
                posterColor = "#1A237E", watchProgress = 0),
            Movie(id = 8, title = "Pulp Fiction", genre = "Crime • Drama",
                rating = 8.9, duration = "2h 34m", year = 1994,
                description = "The lives of mob hitmen, a boxer, and a gangster intertwine in tales of violence.",
                posterColor = "#4A148C", watchProgress = 0),
            Movie(id = 9, title = "Gladiator", genre = "Action • Drama",
                rating = 8.5, duration = "2h 35m", year = 2000,
                description = "A Roman General exacts vengeance against the corrupt emperor who murdered his family.",
                posterColor = "#B71C1C", watchProgress = 20),
            Movie(id = 10, title = "The Matrix", genre = "Sci-Fi • Action",
                rating = 8.7, duration = "2h 16m", year = 1999,
                description = "A hacker learns about the true nature of his reality and his role in a war.",
                posterColor = "#006064", watchProgress = 40),
            Movie(id = 11, title = "Forrest Gump", genre = "Drama • Romance",
                rating = 8.8, duration = "2h 22m", year = 1994,
                description = "An Alabama man with a low IQ witnesses key moments in 20th century American history.",
                posterColor = "#1A237E", watchProgress = 0),
            Movie(id = 12, title = "The Godfather", genre = "Crime • Drama",
                rating = 9.2, duration = "2h 55m", year = 1972,
                description = "An aging patriarch transfers control of his crime empire to his reluctant son.",
                posterColor = "#1B5E20", watchProgress = 0)
        )
    }

    fun searchMovies(query: String): List<Movie> {
        return getAllMovies().filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.genre.contains(query, ignoreCase = true)
        }
    }
}