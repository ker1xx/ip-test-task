package com.example.ip_test_task.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.SystemUiController

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF2E2E2E),
    primary = DarkGreen,
    surface = Color(0xFF2E2E2E),
    onSurface = Color.White,
    secondary = LightPurple,
    tertiary = Red
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    primary = LightGreen,
    surface = Color.White,
    onSurface = Color.Black,
    secondary = DarkPurple,
    tertiary = Red
)

@Composable
fun IptesttaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}