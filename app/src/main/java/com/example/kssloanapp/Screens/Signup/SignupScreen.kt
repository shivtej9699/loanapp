import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kssloanapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFE3F2FD)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var profileBase64 by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
            imageBitmap = bitmap

            // Convert to Base64
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            profileBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),

            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Create Account", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)

                // Profile Image
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { launcher.launch("image/*") }
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("Pick Image", color = Color.White, fontSize = 12.sp)
                    }
                }

                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name", color = textColor) }, modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name", color = textColor) }, modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email", color = textColor) }, modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))
                OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number", color = textColor) }, modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password", color = textColor) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password", color = textColor) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), textStyle = LocalTextStyle.current.copy(color = textColor))

                Button(
                    onClick = {
                        errorMessage = null
                        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }

                        isLoading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val uid = auth.currentUser?.uid
                                    if (uid != null) {
                                        val user = Users(
                                            uid = uid,
                                            firstName = firstName,
                                            lastName = lastName,
                                            emailid = email,
                                            phoneNumber = phoneNumber,
                                            profileImage = profileBase64 ?: "" // base64 image string
                                        )

                                        FirebaseDatabase.getInstance().getReference("users")
                                            .child(uid)
                                            .setValue(user)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                                                navController.navigate("Bottom") {
                                                    popUpTo("LoginScreen") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "User save failed", Toast.LENGTH_SHORT).show()

                                            }
                                    }
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Signup failed"
                                }
                            }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text(if (isLoading) "Signing up..." else "Sign Up", color = Color.White)
                }

                Text("Already have an account? Log in", modifier = Modifier.clickable { navController.navigate("login") }, color = Color(0xFF1976D2))


                errorMessage?.let {
                    Text(text = it, color = Color.Red, fontWeight = FontWeight.SemiBold)
                }


                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}
