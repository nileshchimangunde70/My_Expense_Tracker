package com.personal.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<Expense>)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE strftime('%Y-%m', entryDate / 1000, 'unixepoch') = :yearMonth ORDER BY id ASC")
    fun getExpensesForMonth(yearMonth: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY entryDate DESC")
    fun getAllExpenses(): Flow<List<Expense>>
}
