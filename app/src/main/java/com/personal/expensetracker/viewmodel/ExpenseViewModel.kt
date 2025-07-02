package com.personal.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.personal.expensetracker.data.AppDatabase
import com.personal.expensetracker.data.Expense
import com.personal.expensetracker.data.defaultExpenseEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val expenseDao = AppDatabase.getDatabase(application).expenseDao()
    private val _currentMonth = MutableStateFlow(Calendar.getInstance())
    val currentMonthYearFormatted: String
        get() = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(_currentMonth.value.time)
    private val currentYearMonthDbFormat: String
        get() = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(_currentMonth.value.time)
    private val _monthlyExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val monthlyExpenses: StateFlow<List<Expense>> = _monthlyExpenses.asStateFlow()
    val allExpenses: StateFlow<List<Expense>> = expenseDao.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalOverallSpend: StateFlow<Double> = allExpenses
        .combine(MutableStateFlow(0.0)) { expenses, _ -> expenses.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val yearToDateSpend: StateFlow<Double> = allExpenses
        .combine(MutableStateFlow(0.0)) { expenses, _ ->
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            expenses.filter {
                val cal = Calendar.getInstance().apply { timeInMillis = it.entryDate }
                cal.get(Calendar.YEAR) == currentYear
            }.sumOf { it.amount }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init { loadExpensesForCurrentMonth() }

    fun changeMonth(amount: Int) {
        val calendar = _currentMonth.value.clone() as Calendar
        calendar.add(Calendar.MONTH, amount)
        _currentMonth.value = calendar
        loadExpensesForCurrentMonth()
    }

    fun loadExpensesForCurrentMonth() {
        viewModelScope.launch {
            val expenses = expenseDao.getExpensesForMonth(currentYearMonthDbFormat).first()
            if (expenses.isEmpty()) {
                val newExpenses = defaultExpenseEvents.map { eventName ->
                    Expense(eventName = eventName, amount = 0.0, dueDate = "", isPaid = false, entryDate = _currentMonth.value.timeInMillis)
                }
                _monthlyExpenses.value = newExpenses
            } else { _monthlyExpenses.value = expenses }
        }
    }

    fun updateExpense(expense: Expense) {
        val list = _monthlyExpenses.value.toMutableList()
        val index = list.indexOfFirst { it.eventName == expense.eventName }
        if (index != -1) {
            list[index] = expense
            _monthlyExpenses.value = list
        }
    }

    fun saveMonthlyExpenses() {
        viewModelScope.launch {
            val expensesToSave = _monthlyExpenses.value.map { it.copy(entryDate = _currentMonth.value.timeInMillis) }
            expenseDao.insertAll(expensesToSave)
        }
    }
}
