package com.example.moviestreamingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moviestreamingapp.models.WatchlistItem

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "cinestream.db"
        const val DATABASE_VERSION = 1

        // Table 1: watchlist
        const val TABLE_WATCHLIST = "watchlist"
        const val COL_ID = "id"
        const val COL_MOVIE_ID = "movie_id"
        const val COL_TITLE = "title"
        const val COL_GENRE = "genre"
        const val COL_RATING = "rating"
        const val COL_ADDED_DATE = "added_date"

        // Table 2: watch_progress (FK → watchlist)
        const val TABLE_PROGRESS = "watch_progress"
        const val COL_PROG_ID = "id"
        const val COL_WATCHLIST_ID = "watchlist_id"
        const val COL_PROGRESS = "progress"
        const val COL_LAST_WATCHED = "last_watched"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // F2: Table 1 - watchlist
        db.execSQL("""
            CREATE TABLE $TABLE_WATCHLIST (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_MOVIE_ID INTEGER NOT NULL,
                $COL_TITLE TEXT NOT NULL,
                $COL_GENRE TEXT,
                $COL_RATING REAL,
                $COL_ADDED_DATE TEXT
            )
        """.trimIndent())

        // F2: Table 2 - watch_progress with FK to watchlist
        db.execSQL("""
            CREATE TABLE $TABLE_PROGRESS (
                $COL_PROG_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_WATCHLIST_ID INTEGER NOT NULL,
                $COL_PROGRESS INTEGER DEFAULT 0,
                $COL_LAST_WATCHED TEXT,
                FOREIGN KEY($COL_WATCHLIST_ID) REFERENCES $TABLE_WATCHLIST($COL_ID) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROGRESS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WATCHLIST")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    // F3: CREATE - Add to watchlist
    fun addToWatchlist(item: WatchlistItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_MOVIE_ID, item.movieId)
            put(COL_TITLE, item.title)
            put(COL_GENRE, item.genre)
            put(COL_RATING, item.rating)
            put(COL_ADDED_DATE, item.addedDate)
        }
        val id = db.insert(TABLE_WATCHLIST, null, values)

        // Also insert initial progress record in Table 2
        if (id != -1L) {
            val progressValues = ContentValues().apply {
                put(COL_WATCHLIST_ID, id)
                put(COL_PROGRESS, 0)
                put(COL_LAST_WATCHED, item.addedDate)
            }
            db.insert(TABLE_PROGRESS, null, progressValues)
        }
        return id
    }

    // F3: READ - Get all watchlist items
    fun getAllWatchlist(): List<WatchlistItem> {
        val list = mutableListOf<WatchlistItem>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WATCHLIST ORDER BY $COL_ADDED_DATE DESC", null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    WatchlistItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        movieId = it.getInt(it.getColumnIndexOrThrow(COL_MOVIE_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        genre = it.getString(it.getColumnIndexOrThrow(COL_GENRE)),
                        rating = it.getDouble(it.getColumnIndexOrThrow(COL_RATING)),
                        addedDate = it.getString(it.getColumnIndexOrThrow(COL_ADDED_DATE))
                    )
                )
            }
        }
        return list
    }

    // F3: UPDATE - Update progress
    fun updateProgress(watchlistId: Int, progress: Int, date: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PROGRESS, progress)
            put(COL_LAST_WATCHED, date)
        }
        db.update(TABLE_PROGRESS, values, "$COL_WATCHLIST_ID = ?",
            arrayOf(watchlistId.toString()))
    }

    // F3: DELETE - Remove from watchlist
    fun removeFromWatchlist(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_WATCHLIST, "$COL_ID = ?", arrayOf(id.toString()))
    }

    // F5: Search by title using LIKE
    fun searchWatchlist(query: String): List<WatchlistItem> {
        val list = mutableListOf<WatchlistItem>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WATCHLIST WHERE $COL_TITLE LIKE ? ORDER BY $COL_TITLE ASC",
            arrayOf("%$query%")
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    WatchlistItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        movieId = it.getInt(it.getColumnIndexOrThrow(COL_MOVIE_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        genre = it.getString(it.getColumnIndexOrThrow(COL_GENRE)),
                        rating = it.getDouble(it.getColumnIndexOrThrow(COL_RATING)),
                        addedDate = it.getString(it.getColumnIndexOrThrow(COL_ADDED_DATE))
                    )
                )
            }
        }
        return list
    }

    // F5: Sort by rating using ORDER BY
    fun getWatchlistSortedByRating(): List<WatchlistItem> {
        val list = mutableListOf<WatchlistItem>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WATCHLIST ORDER BY $COL_RATING DESC", null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    WatchlistItem(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        movieId = it.getInt(it.getColumnIndexOrThrow(COL_MOVIE_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        genre = it.getString(it.getColumnIndexOrThrow(COL_GENRE)),
                        rating = it.getDouble(it.getColumnIndexOrThrow(COL_RATING)),
                        addedDate = it.getString(it.getColumnIndexOrThrow(COL_ADDED_DATE))
                    )
                )
            }
        }
        return list
    }

    // Check if movie already in watchlist
    fun isInWatchlist(movieId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_ID FROM $TABLE_WATCHLIST WHERE $COL_MOVIE_ID = ?",
            arrayOf(movieId.toString())
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}