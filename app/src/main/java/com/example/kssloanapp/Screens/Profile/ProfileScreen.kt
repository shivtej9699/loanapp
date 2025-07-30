package com.example.kssloanapp.Screens.Profile

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid
    val dbFirestore = FirebaseFirestore.getInstance()
    val dbRealtime = FirebaseDatabase.getInstance().reference

    var name by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var profileImageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var totalForms by remember { mutableStateOf(0) }
    var approvedForms by remember { mutableStateOf(0) }
    var rejectedForms by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showFullImage by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        uid?.let {
            try {
                dbRealtime.child("users").child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val firstName =
                                snapshot.child("firstName").getValue(String::class.java) ?: ""
                            val lastName =
                                snapshot.child("lastName").getValue(String::class.java) ?: ""
                            val profileBase64 =
                                snapshot.child("profileImage").getValue(String::class.java)
                            if (!profileBase64.isNullOrEmpty()) {
                                try {
                                    val byteArray = Base64.decode(profileBase64, Base64.DEFAULT)
                                    profileImageBitmap =
                                        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            name = if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
                                "$firstName $lastName"
                            } else {
                                "Unknown User"
                            }
                            isLoading = false
                        }

                        override fun onCancelled(error: DatabaseError) {
                            name = "Unknown User"
                            isLoading = false
                        }
                    })

                val forms =
                    dbFirestore.collection("loans").whereEqualTo("userId", uid).get().await()
                totalForms = forms.size()
                approvedForms = forms.count { it.getString("status") == "Approved" }
                rejectedForms = forms.count { it.getString("status") == "Rejected" }

            } catch (e: Exception) {
                e.printStackTrace()
                name = "Unknown User"
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Profile", fontSize = 20.sp) })
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (profileImageBitmap != null) {
                        Image(
                            bitmap = profileImageBitmap!!.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable { showFullImage = true }, // <-- click to enlarge
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.firstOrNull()?.toString()?.uppercase() ?: "?",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            name.ifEmpty { "Unknown User" },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(email, fontSize = 16.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text("Loan Stats", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard("Total", totalForms.toString(), Color(0xFF1976D2))
                    StatCard("Approved", approvedForms.toString(), Color(0xFF388E3C))
                    StatCard("Rejected", rejectedForms.toString(), Color(0xFFD32F2F))
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout Confirmation") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                        showLogoutDialog = false
                    }) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // 🔍 Enlarged image dialog
        // 🔍 Enlarged image popup (small square, center of screen)
        if (showFullImage && profileImageBitmap != null) {
            Dialog(onDismissRequest = { showFullImage = false }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    Image(
                        bitmap = profileImageBitmap!!.asImageBitmap(),
                        contentDescription = "Full Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showFullImage = false },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

    }
}

@Composable
fun StatCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(title, fontSize = 14.sp, color = Color.White)
        }
    }
}
