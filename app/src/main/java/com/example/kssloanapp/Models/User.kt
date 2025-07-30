package com.example.kssloanapp.Models

data class Users(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val emailid: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "" // Base64 format
)

data class LoanFormData(
    val name : String="",
    val user : String="",
    val loanAmount: String = "",
    val monthlyIncome: String = "",
    val nameAsPerPan: String = "",
    val email: String = "",
    val phone: String = "",
    val dob: String = "",
    val panCard: String = "",
    val aadhaar: String = "",
    val gender: String = "",         // e.g., "Male", "Female", "Other"
    val state: String = "",
    val city: String = "",
    val pinCode: String = "",
    val employeeType: String = "",   // e.g., "Salaried", "Self-employed"
    val purpose: String = "",
    val address: String = "",
    val occupation: String="",
    val loanPurpose: String="",
    val documentName: String="",
    val _status: String? = "Processing",
    val age : Int=0,
    val timestamp: Long = 0L,
    var loanId: String? = null,
    val userId: String = "",
    val ConsentGiven: Boolean = false
    
)

