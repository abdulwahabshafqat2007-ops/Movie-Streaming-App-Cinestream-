package com.example.moviestreamingapp.database

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()
    private val HARDCODED_UID = "abdulwahab_user"

    // F2: Save user profile to Firestore
    fun saveUserProfile(name: String, plan: String) {
        val user = hashMapOf(
            "name" to name,
            "plan" to plan,
            "email" to "abdulwahab@example.com"
        )
        db.collection("users").document(HARDCODED_UID).set(user)
            .addOnSuccessListener {
                println("✅ Firestore: User profile saved")
            }
            .addOnFailureListener { e ->
                println("❌ Firestore Error: ${e.message}")
            }
    }

    // F2: Sync watchlist item to Firestore
    fun syncWatchlistItem(movieId: Int, title: String, genre: String, rating: Double) {
        val item = hashMapOf(
            "movieId" to movieId,
            "title" to title,
            "genre" to genre,
            "rating" to rating,
            "userId" to HARDCODED_UID
        )
        db.collection("watchlist_sync")
            .document("${HARDCODED_UID}_$movieId")
            .set(item)
            .addOnSuccessListener {
                println("✅ Firestore: Movie '$title' synced to watchlist_sync")
            }
            .addOnFailureListener { e ->
                println("❌ Firestore Error: ${e.message}")
            }
    }

    // F2: Real-time listener
    fun listenToWatchlist(onUpdate: (List<Map<String, Any>>) -> Unit) {
        db.collection("watchlist_sync")
            .whereEqualTo("userId", HARDCODED_UID)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("❌ Firestore Listener Error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { it.data }
                    onUpdate(items)
                }
            }
    }

    // Delete synced item
    fun removeWatchlistItem(movieId: Int) {
        db.collection("watchlist_sync")
            .document("${HARDCODED_UID}_$movieId")
            .delete()
            .addOnSuccessListener {
                println("✅ Firestore: Movie removed from watchlist_sync")
            }
            .addOnFailureListener { e ->
                println("❌ Firestore Error: ${e.message}")
            }
    }
}