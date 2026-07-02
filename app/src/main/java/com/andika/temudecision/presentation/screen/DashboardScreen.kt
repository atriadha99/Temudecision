package com.andika.temudecision.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.presentation.components.GlassCard
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val studyCount by viewModel.studyCount.collectAsState()
    val criteriaCount by viewModel.criteriaCount.collectAsState()
    val alternativeCount by viewModel.alternativeCount.collectAsState()
    val calculationCount by viewModel.calculationCount.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Banner
        item {
            HeroBanner()
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Studi Kasus",
                    value = studyCount.toString(),
                    icon = Icons.Rounded.FolderOpen,
                    iconBg = SDSSColors.Brand100,
                    iconTint = SDSSColors.Brand500,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Total Kriteria",
                    value = criteriaCount.toString(),
                    icon = Icons.Rounded.Checklist,
                    iconBg = Color(0xFFECFDF5),
                    iconTint = SDSSColors.Success,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Total Alternatif",
                    value = alternativeCount.toString(),
                    icon = Icons.Rounded.People,
                    iconBg = Color(0xFFEFF6FF),
                    iconTint = SDSSColors.Info,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Perhitungan",
                    value = calculationCount.toString(),
                    icon = Icons.Rounded.Calculate,
                    iconBg = Color(0xFFFFFBEB),
                    iconTint = SDSSColors.Warning,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Active Study or Welcome section
        item {
            WelcomeSection()
        }

        // Charts Placeholder
        item {
            ChartsSection()
        }
    }
}

@Composable
fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(SDSSColors.Brand600, Color(0xFF6366F1))
                ),
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    "🌟 Smart Decision Support System (SDSS)",
                    style = SDSSTypography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            Text(
                "Pengambilan Keputusan Universal Menjadi Lebih Cerdas",
                style = SDSSTypography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "SDSS mendukung metode analisis terpopuler: SAW, WP, TOPSIS, SMART, Profile Matching, dan AHP.",
                style = SDSSTypography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Column {
                Text(
                    value,
                    style = SDSSTypography.displayLarge.copy(fontSize = 24.sp),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    label,
                    style = SDSSTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun WelcomeSection() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Mulai Analisis Anda",
                    style = SDSSTypography.headlineMedium
                )
                Text(
                    "Pilih studi kasus yang ada atau buat baru untuk mulai membandingkan alternatif.",
                    style = SDSSTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Navigate to studies */ },
                    colors = ButtonDefaults.buttonColors(containerColor = SDSSColors.Brand500),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Kelola Studi Kasus →")
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("📊", fontSize = 64.sp)
        }
    }
}

@Composable
fun ChartsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Analisis Data", style = SDSSTypography.titleLarge)
        GlassCard(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Charts akan muncul di sini (Vico integration)", style = SDSSTypography.bodyMedium)
            }
        }
    }
}
