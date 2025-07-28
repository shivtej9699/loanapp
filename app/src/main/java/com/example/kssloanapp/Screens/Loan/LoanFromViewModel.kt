package com.example.kssloanapp.Screens.Loan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kssloanapp.Models.LoanFormData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class LoanFormViewModel : ViewModel() {

    val formData = mutableStateOf(LoanFormData())
    val isLoading = mutableStateOf(false)
    val isSuccess = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    val submittedLoanId = mutableStateOf("")

    fun updateField(update: (LoanFormData) -> LoanFormData) {
        formData.value = update(formData.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submitForm(onSuccess: (String) -> Unit, onFailure: (String) -> Unit = {}) {
        isLoading.value = true
        val db = FirebaseFirestore.getInstance()
        val loanData = formData.value
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            isLoading.value = false
            error.value = "User not logged in"
            onFailure("User not logged in")
            return
        }

        val newLoanRef = db.collection("Loans").document() // create a new doc ID
        val loanId = newLoanRef.id

        val dataMap = hashMapOf(
            "loanId" to loanId,
            "userId" to userId,
            "name" to loanData.name,
            "email" to loanData.email,
            "phone" to loanData.phone,
            "loanAmount" to loanData.loanAmount,
            "monthlyIncome" to loanData.monthlyIncome,
            "dob" to loanData.dob,
            "panCard" to loanData.panCard,
            "aadhaar" to loanData.aadhaar,
            "address" to loanData.address,
            "occupation" to loanData.occupation,
            "loanPurpose" to loanData.loanPurpose,
            "gender" to loanData.gender,
            "ConsentGiven" to loanData.ConsentGiven,
            "status" to "Submitted",
            "timestamp" to System.currentTimeMillis()
        )

        newLoanRef.set(dataMap)
            .addOnSuccessListener {
                Log.d("LoanForm", "Form submitted successfully with ID: $loanId")
                isLoading.value = false
                isSuccess.value = true
                submittedLoanId.value = loanId
                onSuccess(loanId)
            }
            .addOnFailureListener {
                isLoading.value = false
                error.value = "Failed to submit form"
                onFailure(it.message ?: "Error")
            }
    }

    fun isFormValid(data: LoanFormData): Boolean {
        return when {
            data.name.isBlank() -> {
                error.value = "Name is required"
                false
            }
            data.email.isBlank() -> {
                error.value = "Email is required"
                false
            }
            data.phone.isBlank() -> {
                error.value = "Phone number is required"
                false
            }
            data.loanAmount.isBlank() -> {
                error.value = "Loan amount is required"
                false
            }
            data.monthlyIncome.isBlank() -> {
                error.value = "Monthly income is required"
                false
            }
            !isValidDate(data.dob) -> {
                error.value = "Please enter a valid DOB in DD/MM/YYYY format"
                false
            }
            data.panCard.isBlank() -> {
                error.value = "PAN Card is required"
                false
            }
            data.aadhaar.isBlank() -> {
                error.value = "Aadhaar is required"
                false
            }
            data.address.isBlank() -> {
                error.value = "Address is required"
                false
            }
            data.occupation.isBlank() -> {
                error.value = "Occupation is required"
                false
            }
            data.gender == "Select" -> {
                error.value = "Please select gender"
                false
            }
            data.loanPurpose == "Select" -> {
                error.value = "Please select loan purpose"
                false
            }
            !data.ConsentGiven -> {
                error.value = "Please accept terms and conditions"
                false
            }
            else -> true
        }
    }

    fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun calculateAgeFromDob(dob: String): Int {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate = sdf.parse(dob)
            val today = Calendar.getInstance()
            val dobCalendar = Calendar.getInstance().apply { time = birthDate!! }

            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: Exception) {
            -1
        }
    }
}
