package com.example.kssloanapp.Screens.Loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLoanScreen(bankName: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(bankName, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00695C))
        )

        // Loan Cards
        LoanCard(
            title = "Instant Personal Loans",
            subtitle = "Apply in minutes and get approved fast!",
            backgroundColor = Color(0xFF0D47A1),
            buttonTextColor = Color(0xFF0D47A1),
            icon = Icons.Default.Bolt,
            loanType = "personal",
            navController = navController
        )

        LoanCard(
            title = "Gold Loan",
            subtitle = "Get loan against your gold with lowest interest.",
            backgroundColor = Color(0xFFFBC02D),
            buttonTextColor = Color(0xFFFBC02D),
            icon = Icons.Default.AttachMoney,
            loanType = "gold",
            navController = navController
        )

        LoanCard(
            title = "Home Loan",
            subtitle = "Buy your dream home with affordable EMIs.",
            backgroundColor = Color(0xFF388E3C),
            buttonTextColor = Color(0xFF388E3C),
            icon = Icons.Default.Home,
            loanType = "home",
            navController = navController
        )

        // Why Choose Us
        Text(
            text = "Why Choose Us?",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )

        FeatureItem(
            icon = Icons.Default.Bolt,
            title = "Quick Approval",
            subtitle = "Get loans approved within minutes."
        )

        FeatureItem(
            icon = Icons.Default.Lock,
            title = "Secure & Safe",
            subtitle = "Your data is 100% secure with us."
        )

        FeatureItem(
            icon = Icons.Default.Phone,
            title = "24/7 Support",
            subtitle = "We're always available to help."
        )

        FeatureItem(
            icon = Icons.Default.AttachMoney,
            title = "Low Interest Rates",
            subtitle = "Get loans at the lowest interest rates."
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun LoanCard(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    buttonTextColor: Color,
    icon: ImageVector,
    loanType: String,
    navController: NavController
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(text = subtitle, color = Color.White, fontSize = 14.sp)

            Button(
                onClick = {
                    navController.navigate("LoanFormScreen/$loanType")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Apply Now", color = buttonTextColor)
            }
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF448AFF),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = Color.Gray, fontSize = 13.sp)
        }
    }
}
