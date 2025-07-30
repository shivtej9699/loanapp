package com.example.kssloanapp
<<<<<<< HEAD
=======
import android.os.Build
>>>>>>> shivtej
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< HEAD
=======
import androidx.annotation.RequiresApi
>>>>>>> shivtej
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kssloanapp.Screens.Navigation.NavGraph
import com.example.kssloanapp.ui.theme.KSSLoanAppTheme
<<<<<<< HEAD


class MainActivity : ComponentActivity() {
=======
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
>>>>>>> shivtej
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KSSLoanAppTheme {
                val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
