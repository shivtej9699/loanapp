package com.example.kssloanapp.Screens.Start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Mann hai\ntoh money hai",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Filled.Money,
                contentDescription = "Man Illustration",
                tint = colorScheme.primary,
                modifier = Modifier.size(140.dp)
            )
        }

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

        Text(
            text = "For a tenure of 3 to 60 months",
            fontSize = 14.sp,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {
                navcontroller.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Get Started", color = colorScheme.onPrimary, fontSize = 16.sp)
        }
    }
}

@Composable
fun FeatureItem(title: String, subtitle: String, icon: ImageVector) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            tint = colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colorScheme.onBackground)
        Text(text = subtitle, fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(navcontroller = rememberNavController())
}
