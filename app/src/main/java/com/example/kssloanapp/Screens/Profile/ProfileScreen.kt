<<<<<<< HEAD
package com.example.kssloanapp.Screens.Profile
=======
package com.example.kssloanapp.Screens.Home
>>>>>>> a3c114c (first)

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
<<<<<<< HEAD
=======
import androidx.compose.ui.platform.LocalContext
>>>>>>> a3c114c (first)
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kssloanapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

<<<<<<< HEAD
// 🔷 ViewModel
=======


>>>>>>> a3c114c (first)
@HiltViewModel
class ProfileViewmodel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    private val _userData = MutableStateFlow<Users?>(null)
    val userData = _userData.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        _loading.value = true
        val uid = auth.currentUser?.uid
        if (uid != null) {
            dbRef.child(uid).get()
                .addOnSuccessListener {
                    _userData.value = it.getValue(Users::class.java)
                    _loading.value = false
                }
                .addOnFailureListener {
                    _userData.value = null
                    _loading.value = false
                }
        } else {
            // no logged-in user
            _userData.value = null
            _loading.value = false
        }
    }

    fun logout() {
        auth.signOut()
        _userData.value = null
    }
}

// 🔷 Composable Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewmodel: ProfileViewmodel = hiltViewModel()
) {
    val user by viewmodel.userData.collectAsState()
    val loading by viewmodel.loading.collectAsState()

    // Refresh user data whenever screen loads
    LaunchedEffect(Unit) {
        viewmodel.fetchUserData()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("My Profile") })
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> CircularProgressIndicator()
                user != null -> ProfileContent(user!!, onLogout = {
                    viewmodel.logout()
                    navController.navigate("Login") {
                        popUpTo("Profile") { inclusive = true }
                    }
                })
                else -> Text("No user data found. Please login/signup.")
            }
        }
    }
}

@Composable
fun ProfileContent(user: Users, onLogout: () -> Unit) {
<<<<<<< HEAD
=======
    val context = LocalContext.current
    val bitmap = try {
        val byteArray = Base64.decode(user.profileImage, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        null
    }
>>>>>>> a3c114c (first)
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
<<<<<<< HEAD
        if (user.profileImage.isNotEmpty()) {

                val byteArray = Base64.decode(user.profileImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.height(16.dp))
            }
=======

//        // ✅ Show profile image or fallback
//        val bitmap = try {
//            val byteArray = Base64.decode(user.profileImage, Base64.DEFAULT)
//            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        } catch (e: Exception) {
//            BitmapFactory.decodeResource(context.resources, R.drawable.defualt_profile) // <-- fallback image
//        }

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
>>>>>>> a3c114c (first)
        }

        val fullName = "${user.firstName.orEmpty()} ${user.lastName.orEmpty()}".trim().ifEmpty { "no name" }
        ProfileItem("Name", fullName)
        Spacer(Modifier.height(16.dp))
        ProfileItem("Email", user.emailid.ifEmpty { "no email" })
        Spacer(Modifier.height(16.dp))
        ProfileItem("Mobile", user.phoneNumber.ifEmpty { "no number" })
        Spacer(Modifier.height(32.dp))

<<<<<<< HEAD
        Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
=======
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}


>>>>>>> a3c114c (first)


@Composable
fun ProfileItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}
