package com.personal.expensetracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.expensetracker.data.Expense
import com.personal.expensetracker.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExpensesScreen(viewModel: ExpenseViewModel, onSave: () -> Unit) {
    val expenses by viewModel.monthlyExpenses.collectAsState()
    val totalAmount = expenses.sumOf { it.amount }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item { TableHeader() }
            items(expenses) { expense ->
                ExpenseRow(expense = expense, onExpenseChange = { viewModel.updateExpense(it) })
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        TotalRow(totalAmount)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.saveMonthlyExpenses(); onSave() }, modifier = Modifier.fillMaxWidth()) {
            Text("SAVE MONTHLY DATA")
        }
    }
}

@Composable
fun TableHeader() {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant), verticalAlignment = Alignment.CenterVertically) {
        Text("Event", modifier = Modifier.weight(2.5f).padding(8.dp), fontWeight = FontWeight.Bold)
        Text("Amount", modifier = Modifier.weight(1.5f).padding(8.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Due Date", modifier = Modifier.weight(1.5f).padding(8.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Paid", modifier = Modifier.weight(0.8f).padding(8.dp), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ExpenseRow(expense: Expense, onExpenseChange: (Expense) -> Unit) {
    var amount by remember(expense.amount) { mutableStateOf(if (expense.amount == 0.0) "" else expense.amount.toString()) }
    var dueDate by remember(expense.dueDate) { mutableStateOf(expense.dueDate) }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(expense.eventName, modifier = Modifier.weight(2.5f))
        OutlinedTextField(value = amount, onValueChange = { amount = it; onExpenseChange(expense.copy(amount = it.toDoubleOrNull() ?: 0.0)) }, modifier = Modifier.weight(1.5f).padding(horizontal = 4.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
        OutlinedTextField(value = dueDate, onValueChange = { dueDate = it; onExpenseChange(expense.copy(dueDate = it)) }, modifier = Modifier.weight(1.5f).padding(horizontal = 4.dp), singleLine = true, placeholder = { Text("dd/MM") })
        Checkbox(checked = expense.isPaid, onCheckedChange = { onExpenseChange(expense.copy(isPaid = it)) }, modifier = Modifier.weight(0.8f))
    }
}

@Composable
fun TotalRow(total: Double) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Approx. Total", modifier = Modifier.weight(2.5f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = currencyFormat.format(total), modifier = Modifier.weight(3.8f), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
    }
}
