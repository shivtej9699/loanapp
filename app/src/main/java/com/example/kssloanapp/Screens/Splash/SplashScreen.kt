package com.example.kssloanapp.Screens.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kssloanapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val isDark = isSystemInDarkTheme()

    // Use your app's R class here
    val logoImage = if (isDark) R.drawable.logo else R.drawable.logo
    val kayalogo = if (isDark) R.drawable.logo else R.drawable.logo

    LaunchedEffect(true) {
        delay(1000)
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.token != null) {
                navController.navigate("WelcomeScreen") {
                    popUpTo("SplashScreen") { inclusive = true }
                }
            } else {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("WelcomeScreen") {
                    popUpTo("SplashScreen") { inclusive = true }
                }
            }
        } ?: run {
            navController.navigate("WelcomeScreen") {
                popUpTo("SplashScreen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Center Logo
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = logoImage),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )
        }

        // Bottom Text and Meta Logo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "from",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = kayalogo),
                contentDescription = "Meta Logo",
                modifier = Modifier.height(20.dp)
            )
        }
    }
}