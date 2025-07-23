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
import androidx.compose.ui.graphics.painter.Painter
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

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var profileImageBase64 by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                // Convert bitmap to Base64
                val stream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                profileImageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Create Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                // Profile Image Picker
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                    } else {
                        Text("Pick Image", fontSize = 12.sp)
                    }
                }

                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

                Button(
                    onClick = {

                        errorMessage = null

                        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() ||
                            phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()
                        ) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        if (!isEmailValid) {
                            Toast.makeText(
                                context,
                                "Please enter a valid email",
                                Toast.LENGTH_SHORT
                            ).show()
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
                                            profileImage = profileImageBase64 // 🔥 save image
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
                                                Toast.makeText(context, "User data save failed", Toast.LENGTH_SHORT).show()
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

                Text("Already have an account? Log in", modifier = Modifier.clickable { navController.navigate("login") })

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
