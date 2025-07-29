package com.example.kssloanapp.Screens.Navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.example.kssloanapp.Models.NavItems
import com.example.kssloanapp.Screens.ChatBot.ChatScreen
import com.example.kssloanapp.Screens.Home.HomeScreen
import com.example.kssloanapp.Screens.Loan.LoanFormListScreen
import com.example.kssloanapp.Screens.Profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavController) {
    val navItems = listOf(
        NavItems("Home", Icons.Default.Home),
        NavItems("Form", Icons.Default.Forum),
        NavItems("ChatBot", Icons.Default.Chat),
        NavItems("Profile", Icons.Default.Person)
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val isKeyboardVisible = isKeyboardOpen()
    var showChatBot by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Launch ChatBot expand only once per open
    LaunchedEffect(showChatBot) {
        if (showChatBot) {
            coroutineScope.launch {
                sheetState.expand()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isKeyboardVisible) {
                NavigationBar {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                if (item.label == "ChatBot") {
                                    showChatBot = true
                                } else {
                                    showChatBot = false
                                    selectedIndex = index
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(text = item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding() // handle keyboard + bottom inset
                .fillMaxSize()
        ) {
            when (selectedIndex) {
                0 -> HomeScreen(navController = navController)
                1 -> LoanFormListScreen(navController = navController)
                3 -> ProfileScreen(navController = navController)
            }

            if (showChatBot) {
                ModalBottomSheet(
                    onDismissRequest = { showChatBot = false },
                    sheetState = sheetState,
                    windowInsets = WindowInsets(0) // removes extra padding
                ) {
                    ChatScreen(
                        onBackClick = {
                            showChatBot = false // dismiss chatbot on back
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun isKeyboardOpen(): Boolean {
    val view = LocalView.current
    var isOpen by remember { mutableStateOf(false) }

    DisposableEffect(view) {
        val listener = ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            isOpen = insets.isVisible(WindowInsetsCompat.Type.ime())
            insets
        }

        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }

    return isOpen
}
