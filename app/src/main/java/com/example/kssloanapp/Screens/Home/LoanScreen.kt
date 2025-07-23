package com.example.kssloanapp.Screens.Home


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoanApplicationForm(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore

    var loanAmount by remember { mutableStateOf("") }
    var monthlyIncome by remember { mutableStateOf("") }
    var nameOnPan by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var pancard by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var employeeType by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var consentGiven by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")
    val stateOptions = listOf("Maharashtra", "Karnataka", "Delhi", "Other")
    val employeeOptions = listOf("Salaried", "Self-Employed", "Unemployed")
    val purposeOptions = listOf("Personal", "Business", "Education", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = loanAmount, onValueChange = { loanAmount = it }, label = { Text("Loan Amount") })
        OutlinedTextField(value = monthlyIncome, onValueChange = { monthlyIncome = it }, label = { Text("Monthly Income") })
        OutlinedTextField(value = nameOnPan, onValueChange = { nameOnPan = it }, label = { Text("Name as per PAN") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email ID") })
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth (dd-mm-yyyy)") })
        OutlinedTextField(value = pancard, onValueChange = { pancard = it }, label = { Text("PAN Card") })
        OutlinedTextField(value = aadhaar, onValueChange = { aadhaar = it }, label = { Text("Aadhaar Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        DropdownMenuBox(label = "Gender", options = genderOptions, selectedOption = gender) { gender = it }
        DropdownMenuBox(label = "State", options = stateOptions, selectedOption = state) { state = it }
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
        OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        DropdownMenuBox(label = "Employee Type", options = employeeOptions, selectedOption = employeeType) { employeeType = it }
        DropdownMenuBox(label = "Purpose", options = purposeOptions, selectedOption = purpose) { purpose = it }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentGiven, onCheckedChange = { consentGiven = it })
            Text(
                " I hereby give consent...",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Button(
            onClick = {
                if (!consentGiven) {
                    Toast.makeText(context, "Please agree to terms", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val formData = hashMapOf(
                    "loanAmount" to loanAmount,
                    "monthlyIncome" to monthlyIncome,
                    "nameOnPan" to nameOnPan,
                    "email" to email,
                    "phone" to phone,
                    "dob" to dob,
                    "pancard" to pancard,
                    "aadhaar" to aadhaar,
                    "gender" to gender,
                    "state" to state,
                    "city" to city,
                    "pincode" to pincode,
                    "employeeType" to employeeType,
                    "purpose" to purpose,
                    "consentGiven" to consentGiven
                )

                db.collection("loanApplications")
                    .add(formData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Form submitted successfully", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Submission failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}