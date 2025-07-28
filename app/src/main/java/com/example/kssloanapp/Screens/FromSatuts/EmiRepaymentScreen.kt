package com.example.kssloanapp.Screens.FromStatus

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EMIRepaymentScreen(loanId: String, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    var loanAmount by remember { mutableStateOf(0.0) }
    var tenure by remember { mutableStateOf(12) }
    var paidEmis by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("Loading...") }
    var error by remember { mutableStateOf(false) }

    // 🔄 Loan Data Fetch
    LaunchedEffect(loanId) {
        if (loanId.isNotEmpty()) {
            db.collection("Loans").document(loanId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        loanAmount = doc.getDouble("loanAmount") ?: 0.0
                        tenure = (doc.getLong("loanTenure") ?: 12L).toInt()
                        paidEmis = (doc.getLong("paidEmis") ?: 0L).toInt()
                        status = doc.getString("status") ?: "Unknown"
                        error = false
                    } else {
                        error = true
                    }
                }
                .addOnFailureListener { e ->
                    error = true
                    Log.e("EMIRepaymentScreen", "Error fetching loan: ", e)
                }
        } else {
            error = true
        }
    }

    val emiAmount = if (loanAmount > 0) calculateEmi(loanAmount, tenure) else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EMI Repayment") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (error) {
                Text("❌ Loan not found or invalid ID!", color = MaterialTheme.colorScheme.error)
                return@Column
            }

            Text("Loan ID: $loanId", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loan Amount: ₹${loanAmount.toInt()}")
            Text("Monthly EMI: ₹$emiAmount")
            Text("EMIs Paid: $paidEmis / $tenure")
            Spacer(modifier = Modifier.height(16.dp))

            if (status == "Repaid") {
                Text("Loan Fully Repaid ✅", color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = {
                    val newPaidEmis = paidEmis + 1
                    val newStatus = if (newPaidEmis >= tenure) "Repaid" else "Approved"
                    db.collection("Loans").document(loanId).update(
                        mapOf(
                            "paidEmis" to newPaidEmis,
                            "status" to newStatus
                        )
                    ).addOnSuccessListener {
                        paidEmis = newPaidEmis
                        status = newStatus
                    }
                }) {
                    Text("Pay EMI Now")
                }
            }
        }
    }
}

fun calculateEmi(loanAmount: Double, tenure: Int): Int {
    val interestRate = 0.10 // 10% yearly
    val emi = (loanAmount * (1 + interestRate)) / tenure
    return emi.toInt()
}
