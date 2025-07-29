package com.example.kssloanapp.Screens.ChatBot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// --- ViewModel ---
class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        _messages.add(ChatMessage(text, true))
        getBotReply(text)
    }

    private fun getBotReply(userMsg: String) {
        val lowerMsg = userMsg.lowercase()
        val reply = when {
            listOf("apply", "how to apply").any { lowerMsg.contains(it) } ->
                "You can apply for a loan from the Apply tab. Fill in your personal and income details."

            listOf("status", "track", "application").any { lowerMsg.contains(it) } ->
                "Check your loan application status under the Home tab."

            listOf("documents", "required", "pan", "aadhar").any { lowerMsg.contains(it) } ->
                "Documents usually required: PAN, Aadhaar, Income Proof, Bank Statement."

            listOf("interest", "rate").any { lowerMsg.contains(it) } ->
                "Interest rates vary between 8% to 14% based on your eligibility."

            listOf("eligibility", "eligible").any { lowerMsg.contains(it) } ->
                "Eligibility depends on your income, credit score, and employment type."

            listOf("emi", "repayment").any { lowerMsg.contains(it) } ->
                "EMIs start the month after loan disbursal. Use EMI calculator in app."

            listOf("approve", "approved", "sanction").any { lowerMsg.contains(it) } ->
                "Loan approval usually takes 1–2 working days after verification."

            listOf("reject", "rejected", "decline").any { lowerMsg.contains(it) } ->
                "Loan rejection could be due to incomplete info, low income, or low credit score."

            listOf("delay", "late", "fine").any { lowerMsg.contains(it) } ->
                "Late EMI payment can attract penalty charges. Please repay on time."

            else ->
                "Sorry, I didn’t get that. Ask me about loan apply, EMI, interest, or eligibility."
        }
        _messages.add(ChatMessage(reply, false))
    }
}