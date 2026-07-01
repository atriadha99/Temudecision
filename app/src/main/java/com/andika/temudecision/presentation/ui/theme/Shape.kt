package com.andika.temudecision.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val SDSSShapes = Shapes(
    small = RoundedCornerShape(8.dp),     // Small chips, badges
    medium = RoundedCornerShape(12.dp),    // Buttons, inputs (rounded-xl)
    large = RoundedCornerShape(16.dp),     // Cards (rounded-2xl)
    extraLarge = RoundedCornerShape(24.dp) // Banner cards (rounded-3xl)
)
