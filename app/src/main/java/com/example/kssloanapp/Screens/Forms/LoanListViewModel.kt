package com.example.kssloanapp.Screens.Forms

import androidx.lifecycle.ViewModel
import com.example.kssloanapp.Models.LoanFormData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoanListViewModel : ViewModel() {

    private val _loanList = MutableStateFlow<List<LoanFormData>>(emptyList())
    val loanList: StateFlow<List<LoanFormData>> = _loanList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUserLoans() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        _isLoading.value = true

        FirebaseFirestore.getInstance()
            .collection("Loans")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                val loans = documents.mapNotNull { doc ->
                    val loan = doc.toObject(LoanFormData::class.java)
                    loan.loanId = doc.id  // firestore doc ID सेट
                    loan
                }
                _loanList.value = loans
                _isLoading.value = false
            }
            .addOnFailureListener {
                _loanList.value = emptyList()
                _isLoading.value = false
            }
    }
}
