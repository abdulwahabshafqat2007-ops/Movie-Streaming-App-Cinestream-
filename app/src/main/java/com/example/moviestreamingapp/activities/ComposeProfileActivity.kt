package com.example.moviestreamingapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ComposeProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ComposeProfileScreen(onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    finish()
                    startActivity(android.content.Intent(this, LoginActivity::class.java))
                })
            }
        }
    }
}

@Composable
fun ComposeProfileScreen(onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var userName by remember { mutableStateOf("Loading...") }
    var userPlan by remember { mutableStateOf("") }

    // Fetch from Firestore
    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("users").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    userName = doc.getString("name") ?: user.email ?: "User"
                    userPlan = doc.getString("plan") ?: "Free"
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Avatar circle
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE50914)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.firstOrNull()?.toString() ?: "U",
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(userName, fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text(user?.email ?: "", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                "Plan: $userPlan",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                color = Color(0xFFE50914),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.White)
        }
    }
}