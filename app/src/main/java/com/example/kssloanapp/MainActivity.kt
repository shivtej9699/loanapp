package com.example.kssloanapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kssloanapp.Screens.Navigation.NavGraph
import com.example.kssloanapp.ui.theme.KSSLoanAppTheme


class MainActivity : ComponentActivity() {
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
