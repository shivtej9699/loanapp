package com.example.kssloanapp.Screens.FromStatus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStatusScreen(loanId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var status by remember { mutableStateOf("Loading...") }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    DisposableEffect(loanId) {
        val listener = db.collection("Loans").document(loanId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    status = "Error"
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    status = snapshot.getString("status") ?: "No Status Found"
                } else {
                    status = "No Status Found"
                }
            }

        listenerRegistration = listener
        onDispose { listenerRegistration?.remove() }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Loan Status") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            if (status == "Approved") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Approved",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(100.dp)
                        )

                        Text(
                            text = "Congratulations!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF2C3E50)
                        )

                        Text(
                            text = "Your loan application has been approved!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )

                        Button(
                            onClick = {
                                navController.navigate("emi_repayment/$loanId")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text(text = "Proceed to EMI", fontSize = 16.sp, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = {
                                navController.navigate("LoanDetailsScreen")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50))
                        ) {
                            Text(text = "View Details", fontSize = 16.sp)
                        }
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Loan Status is:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = status,
                        style = MaterialTheme.typography.headlineMedium,
                        color = when (status) {
                            "Rejected" -> MaterialTheme.colorScheme.error
                            "Processing" -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}
