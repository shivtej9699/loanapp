package com.example.kssloanapp.Models


import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(label: String, date: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year: Int
    val month: Int
    val day: Int

    if (date.isNotEmpty()) {
        val parts = date.split("/")
        day = parts.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
        month = (parts.getOrNull(1)?.toIntOrNull() ?: calendar.get(Calendar.MONTH + 1)) - 1
        year = parts.getOrNull(2)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)
    } else {
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    var selectedDate by remember { mutableStateOf(date) }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        selectedDate = "$d/${m + 1}/$y"
                        onDateSelected(selectedDate)
                    },
                    year,
                    month,
                    day
                ).show()
            },
        colors = OutlinedTextFieldDefaults.colors()
    )
}
