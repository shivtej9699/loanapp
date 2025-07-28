package com.example.kssloanapp.Screens.Loan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kssloanapp.Models.LoanFormData
import com.example.kssloanapp.Screens.Forms.LoanListViewModel

// ✅ Safe extension property
val LoanFormData.status: String
    get() = this._status ?: "Processing"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanFormListScreen(navController: NavController) {
    val viewModel: LoanListViewModel = viewModel()
    val loanList by viewModel.loanList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserLoans()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Loan Applications") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                if (loanList.isEmpty()) {
                    Text("No loan applications found.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(loanList) { loan ->
                            LoanCard(loan = loan) {
                                loan.loanId?.let { loanId ->
                                    navController.navigate("loan_status/$loanId")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoanCard(loan: LoanFormData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${loan.name}")
            Text(text = "Amount: ₹${loan.loanAmount}")
            Text(text = "Status: ${loan.status}")
            Text(text = "Purpose: ${loan.loanPurpose}")
        }
    }
}
