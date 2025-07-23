package com.example.kssloanapp.Screens.Login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun WelcomeScreen(navcontroller: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Top Text
        Text(
            text = "Mann hai\ntoh money hai",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B153E),
            textAlign = TextAlign.Center
        )

        // Main illustration (Placeholder circle)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(240.dp)
                .background(Color(0xFFFFE7E0), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Money,
                contentDescription = "Man Illustration",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(140.dp)
            )
        }

        // Features Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureItem("Personal Loan", "Up to ₹10L", Icons.Filled.Money)
            FeatureItem("Fixed Deposit", "Up to 9.30%", Icons.Filled.AccountBalance)
            FeatureItem("Credit Tracker", "Free Report", Icons.Filled.CreditCard)
        }

        // Duration Text
        Text(
            text = "For a tenure of 3 to 60 months",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        // Get Started Button
        Button(
            onClick = {

                navcontroller.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            colors = ButtonDefaults.buttonColors(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Get Started", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun FeatureItem(title: String, subtitle: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            tint = Color(0xFF1B1B1B)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(navcontroller = rememberNavController())
}