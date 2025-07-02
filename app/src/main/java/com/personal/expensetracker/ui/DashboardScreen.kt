package com.personal.expensetracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.pie.PieChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.pie.PieEntry
import com.personal.expensetracker.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(viewModel: ExpenseViewModel) {
    val allExpenses by viewModel.allExpenses.collectAsState()
    val totalOverall by viewModel.totalOverallSpend.collectAsState()
    val ytdSpend by viewModel.yearToDateSpend.collectAsState()
    val monthlyExpenses by viewModel.monthlyExpenses.collectAsState()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val categoryTotals = allExpenses.filter { it.amount > 0 }.groupBy { it.eventName }.mapValues { entry -> entry.value.sumOf { it.amount } }
    val pieChartEntries = categoryTotals.map { (name, total) -> PieEntry(total.toFloat(), name) }
    val chartModelProducer = ChartEntryModelProducer(pieChartEntries)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Dashboard", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            KpiCard("Total Spent (Overall)", currencyFormat.format(totalOverall), Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            KpiCard("Total Spent (YTD)", currencyFormat.format(ytdSpend), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        val currentMonthSpend = monthlyExpenses.sumOf { it.amount }
        KpiCard("Spend This Month (${viewModel.currentMonthYearFormatted})", currencyFormat.format(currentMonthSpend), Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        Text("Spending by Category", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        if (pieChartEntries.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().height(300.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                Chart(chart = PieChart(), chartModelProducer = chartModelProducer)
            }
        } else { Text("No spending data yet to display chart.") }
    }
}

@Composable
fun KpiCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}
