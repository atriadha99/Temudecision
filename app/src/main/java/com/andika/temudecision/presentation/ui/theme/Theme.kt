package com.andika.temudecision.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SDSSColors.Brand500,
    secondary = SDSSColors.Brand300,
    tertiary = SDSSColors.Brand700,
    background = SDSSColors.DarkBg,
    surface = SDSSColors.DarkCard,
    onPrimary = SDSSColors.DarkTextPrimary,
    onSecondary = SDSSColors.DarkTextSecondary,
    onTertiary = SDSSColors.DarkTextTertiary,
    onBackground = SDSSColors.DarkTextPrimary,
    onSurface = SDSSColors.DarkTextPrimary,
    outline = SDSSColors.DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SDSSColors.Brand500,
    secondary = SDSSColors.Brand300,
    tertiary = SDSSColors.Brand700,
    background = SDSSColors.LightBg,
    surface = SDSSColors.LightCard,
    onPrimary = SDSSColors.LightTextPrimary,
    onSecondary = SDSSColors.LightTextSecondary,
    onTertiary = SDSSColors.LightTextTertiary,
    onBackground = SDSSColors.LightTextPrimary,
    onSurface = SDSSColors.LightTextPrimary,
    outline = SDSSColors.LightBorder

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TemuDecisionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SDSSTypography,
        shapes = SDSSShapes,
        content = content
    )
}
