package com.example.moviestreamingapp.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.database.DatabaseHelper
import com.example.moviestreamingapp.database.FirestoreHelper
import com.example.moviestreamingapp.databinding.FragmentMovieDetailBinding
import com.example.moviestreamingapp.models.Movie
import com.example.moviestreamingapp.models.WatchlistItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private var movie: Movie? = null
    private val firestoreHelper = FirestoreHelper()

    companion object {
        fun newInstance(movie: Movie): MovieDetailFragment {
            return MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("MOVIE", movie)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getSerializable("MOVIE") as? Movie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = DatabaseHelper(requireContext())

        movie?.let { currentMovie ->
            binding.apply {
                detailTitle.text = currentMovie.title
                detailGenre.text = currentMovie.genre
                detailRating.text = "Rating: ⭐ ${currentMovie.rating}"
                detailYear.text = "Year: ${currentMovie.year}"
                detailDuration.text = "Duration: ${currentMovie.duration}"
                detailDescription.text = currentMovie.description
                detailWatchProgress.progress = currentMovie.watchProgress
                posterView.setBackgroundColor(Color.parseColor(currentMovie.posterColor))

                backButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }

                // Check if already in watchlist
                lifecycleScope.launch(Dispatchers.IO) {
                    val inWatchlist = dbHelper.isInWatchlist(currentMovie.id)
                    withContext(Dispatchers.Main) {
                        btnWatchlist.text = if (inWatchlist) "✓ In Watchlist" else "+ Watchlist"
                    }
                }

                // Add/check watchlist on click
                btnWatchlist.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val inWatchlist = dbHelper.isInWatchlist(currentMovie.id)
                        if (!inWatchlist) {
                            val date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault()
                            ).format(Date())
                            dbHelper.addToWatchlist(
                                WatchlistItem(
                                    movieId = currentMovie.id,
                                    title = currentMovie.title,
                                    genre = currentMovie.genre,
                                    rating = currentMovie.rating,
                                    addedDate = date
                                )
                            )

                            // Self-researched Feature 1: Sync to Firestore
                            firestoreHelper.syncWatchlistItem(
                                movieId = currentMovie.id,
                                title = currentMovie.title,
                                genre = currentMovie.genre,
                                rating = currentMovie.rating
                            )

                            withContext(Dispatchers.Main) {
                                btnWatchlist.text = "✓ In Watchlist"
                                Toast.makeText(
                                    requireContext(),
                                    "${currentMovie.title} added to Watchlist",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Self-researched Feature 1: Local push notification
                                sendLocalNotification(requireContext(), currentMovie.title)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Already in Watchlist",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // Self-researched Feature 1: Local notification triggered on watchlist add
    private fun sendLocalNotification(context: Context, title: String) {
        val channelId = "watchlist_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Watchlist Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Added to Watchlist")
            .setContentText("\"$title\" was added to your watchlist!")
            .setAutoCancel(true)
            .build()

        manager.notify(title.hashCode(), notification)
    }
}