package com.personal.expensetracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.personal.expensetracker.ui.DashboardScreen
import com.personal.expensetracker.ui.ExpensesScreen
import com.personal.expensetracker.ui.theme.MyExpenseTrackerTheme
import com.personal.expensetracker.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ExpenseViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ExpenseViewModel(application) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyExpenseTrackerTheme {
                AppShell(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val currentMonthYear by remember { derivedStateOf { viewModel.currentMonthYearFormatted } }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    Text(text = if (currentRoute == "expenses") "Expenses: $currentMonthYear" else "Dashboard")
                },
                navigationIcon = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    if(currentRoute == "expenses"){
                        IconButton(onClick = { viewModel.changeMonth(-1) }) { Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month") }
                    }
                },
                actions = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    if(currentRoute == "expenses"){
                        IconButton(onClick = { viewModel.changeMonth(1) }) { Icon(Icons.Default.ArrowForward, contentDescription = "Next Month") }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer, titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            )
        },
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "expenses") {
                composable("expenses") { ExpensesScreen(viewModel = viewModel) { Toast.makeText(context, "Data Saved!", Toast.LENGTH_SHORT).show() } }
                composable("dashboard") { DashboardScreen(viewModel = viewModel) }
            }
        }
    }
}

@Composable
fun AppBottomBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        NavigationBarItem(icon = { Icon(Icons.Filled.List, contentDescription = "Expenses") }, label = { Text("Expenses") }, selected = currentRoute == "expenses",
            onClick = { navController.navigate("expenses") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } })
        NavigationBarItem(icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") }, label = { Text("Dashboard") }, selected = currentRoute == "dashboard",
            onClick = { navController.navigate("dashboard") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } })
    }
}
