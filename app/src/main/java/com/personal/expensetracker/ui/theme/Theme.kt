package com.personal.expensetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFFBB86FC),
        secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
        tertiary = androidx.compose.ui.graphics.Color(0xFF3700B3)
)

@Composable
fun MyExpenseTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
