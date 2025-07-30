package com.example.kssloanapp.Screens.Loan

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanFormScreen(
    loanType: String,
    viewModel: LoanFormViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val data = viewModel.formData.value
    val isLoading = viewModel.isLoading.value
    val isSuccess = viewModel.isSuccess.value
    val error = viewModel.error.value

    val isValidDob = viewModel.isValidDate(data.dob)
    val age = if (isValidDob) viewModel.calculateAgeFromDob(data.dob).toString() else ""

    val isSubmitEnabled = !isLoading && data.name.isNotBlank() &&
            data.email.isNotBlank() &&
            data.phone.isNotBlank() &&
            data.loanAmount.isNotBlank() &&
            data.monthlyIncome.isNotBlank() &&
            isValidDob &&
            data.panCard.isNotBlank() &&
            data.aadhaar.isNotBlank() &&
            data.address.isNotBlank() &&
            data.occupation.isNotBlank() &&
            data.loanPurpose != "Select" &&
            data.gender != "Select" &&
            data.ConsentGiven

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${loanType.replaceFirstChar { it.uppercase() }} Loan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Fill in the form below to apply for your $loanType loan.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldWithLabel("Full Name", data.name) { viewModel.updateField { from->from.copy(name = it) } }
            OutlinedTextFieldWithLabel("Email", data.email) { viewModel.updateField { from->from.copy(email = it) } }
            OutlinedTextFieldWithLabel("Phone Number", data.phone) { viewModel.updateField { from->from.copy(phone = it) } }
            OutlinedTextFieldWithLabel("Loan Amount", data.loanAmount) { viewModel.updateField { from->from.copy(loanAmount = it) } }
            OutlinedTextFieldWithLabel("Monthly Income", data.monthlyIncome) { viewModel.updateField { from->from.copy(monthlyIncome = it) } }

            OutlinedTextField(
                value = data.dob,
                onValueChange = { viewModel.updateField { from->from.copy(dob = it) } },
                label = { Text("Date of Birth") },
                placeholder = { Text("dd/MM/yyyy") },
                isError = !isValidDob,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (age.isNotEmpty()) {
                Text("Age: $age", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
            }

            OutlinedTextFieldWithLabel("PAN Card", data.panCard) { viewModel.updateField { from->from.copy(panCard = it) } }
            OutlinedTextFieldWithLabel("Aadhaar Number", data.aadhaar) { viewModel.updateField { from->from.copy(aadhaar = it) } }
            OutlinedTextFieldWithLabel("Address", data.address) { viewModel.updateField { from->from.copy(address = it) } }
            OutlinedTextFieldWithLabel("Occupation", data.occupation) { viewModel.updateField { from->from.copy(occupation = it) } }

            val loanPurposeOptions = listOf("Select", "Home", "Personal", "Education", "Business")
            Text("Loan Purpose", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            DropdownField("Loan Purpose", loanPurposeOptions, data.loanPurpose) {
                viewModel.updateField { from->from.copy(loanPurpose = it) }
            }

            val genderOptions = listOf("Select", "Male", "Female", "Other")
            Text("Gender", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            DropdownField("Gender", genderOptions, data.gender) {
                viewModel.updateField { from->from.copy(gender = it) }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Checkbox(
                    checked = data.ConsentGiven,
                    onCheckedChange = { viewModel.updateField { from ->from.copy(ConsentGiven = it) } }
                )
                Text("I agree to the terms and conditions")
            }

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            if (isSuccess) {
                Text("Form submitted successfully!", color = Color(0xFF2E7D32), modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = {


                    if (viewModel.isFormValid(viewModel.formData.value)) {
                        viewModel.submitForm(
                            onSuccess = { loanId ->
                                navController.navigate("loan_status/$loanId")
                            },
                            onFailure = { errorMsg ->
                                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = !viewModel.isLoading.value
            ) {
                if (viewModel.isLoading.value) {
                    Text("Submitting...")
                } else {
                    Text("Submit")
                }
            }


            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun OutlinedTextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
