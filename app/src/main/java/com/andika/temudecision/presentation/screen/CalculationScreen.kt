package com.andika.temudecision.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.domain.model.RankingResult
import com.andika.temudecision.presentation.components.GlassCard
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.CalculationViewModel
import java.util.Locale

@Composable
fun CalculationScreen(
    studyId: String,
    viewModel: CalculationViewModel = hiltViewModel()
) {
    val results by viewModel.rankingResults.collectAsState()
    val selectedMethod by viewModel.selectedMethod.collectAsState()
    val methods = listOf("SAW", "WP", "TOPSIS", "SMART", "Profile Matching", "AHP", "MOORA")

    LaunchedEffect(studyId, selectedMethod) {
        viewModel.calculate(studyId, selectedMethod)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Perhitungan & Hasil",
            style = SDSSTypography.headlineLarge
        )
        Text(
            "Hasil akhir perangkingan berdasarkan metode yang dipilih.",
            style = SDSSTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Method Tab Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            methods.forEach { method ->
                val isSelected = selectedMethod == method
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.calculate(studyId, method) },
                    label = { Text(method) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SDSSColors.Brand500,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (results.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Tidak ada data untuk dihitung. Pastikan Kriteria, Alternatif, dan Penilaian sudah terisi.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Recommendation Card
                item {
                    val best = results.firstOrNull()
                    if (best != null) {
                        RecommendationCard(best, selectedMethod)
                    }
                }

                item {
                    Text("Hasil Ranking", style = SDSSTypography.titleLarge)
                }

                items(results) { result ->
                    RankingItem(result)
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(result: RankingResult, method: String) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFEF3C7), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = Color(0xFFD97706))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "🏆 Rekomendasi Utama ($method)",
                    style = SDSSTypography.labelSmall,
                    color = Color(0xFFD97706)
                )
                Text(
                    result.alternativeName,
                    style = SDSSTypography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Peringkat pertama dengan skor preferensi: ${String.format(Locale.ROOT, "%.4f", result.score)}",
                    style = SDSSTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RankingItem(result: RankingResult) {
    val scoreText = remember(result.score) { String.format(Locale.ROOT, "%.4f", result.score) }
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        when (result.rank) {
                            1 -> Color(0xFFFEF3C7)
                            2 -> Color(0xFFF1F5F9)
                            3 -> Color(0xFFFFEDD5)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "#${result.rank}",
                    style = SDSSTypography.labelSmall,
                    color = when (result.rank) {
                        1 -> Color(0xFF92400E)
                        2 -> Color(0xFF475569)
                        3 -> Color(0xFF9A3412)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                result.alternativeName,
                style = SDSSTypography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                scoreText,
                style = SDSSTypography.titleMedium,
                color = SDSSColors.Brand500
            )
        }
    }
}
