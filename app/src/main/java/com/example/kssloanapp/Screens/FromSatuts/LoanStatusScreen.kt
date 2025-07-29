package com.example.kssloanapp.Screens.FromStatus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
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
            when (status) {
                "Approved" -> ApprovedCard(loanId, navController)
                "Rejected" -> RejectedCard(navController)
                "Processing" -> ProcessingCard(navController)
                else -> Text(text = status)
            }
        }
    }
}

@Composable
fun ApprovedCard(loanId: String, navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Approved",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(100.dp)
            )

            Text("Congratulations!", style = MaterialTheme.typography.headlineSmall)
            Text("Your loan application is approved.", color = Color.Gray)

            Button(
                onClick = { navController.navigate("emi_repayment/$loanId") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Proceed to EMI", color = Color.White)
            }

            OutlinedButton(
                onClick = { navController.navigate("LoanDetailsScreen") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Details")
            }

            OutlinedButton(
                onClick = { navController.navigate("Bottom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun RejectedCard(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Rejected",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(100.dp)
            )

            Text("Sorry!", style = MaterialTheme.typography.headlineSmall)
            Text("Your loan application was rejected.", color = Color.Gray)

            OutlinedButton(
                onClick = { navController.navigate("Bottom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun ProcessingCard(navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.HourglassEmpty,
                contentDescription = "Processing",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(100.dp)
            )

            Text("Please Wait", style = MaterialTheme.typography.headlineSmall)
            Text("Your loan application is under review.", color = Color.Gray)

            OutlinedButton(
                onClick = { navController.navigate("Bottom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}
