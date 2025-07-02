package com.personal.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    var amount: Double,
    var dueDate: String,
    var isPaid: Boolean,
    val entryDate: Long // Timestamp when this was saved
)

val defaultExpenseEvents = listOf(
    "Car Loan", "CredC3", "Bajaj Phone Loan", "Rent",
    "SBI Credit Card", "ICICI Credit Card", "Axis Credit Card",
    "Electricity Bill", "LIC", "Wifi", "Misc", "Other"
)
