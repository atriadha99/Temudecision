package com.andika.temudecision.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val requiresStudy: Boolean = false
)

@Composable
fun SidebarDrawer(
    activeRoute: String,
    hasActiveStudy: Boolean,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val items = listOf(
        NavigationItem("Dashboard", Icons.Rounded.Dashboard, "dashboard"),
        NavigationItem("Studi Kasus", Icons.Rounded.FolderOpen, "studies"),
        NavigationItem("Kriteria", Icons.Rounded.Checklist, "criteria", true),
        NavigationItem("Alternatif", Icons.Rounded.People, "alternatives", true),
        NavigationItem("Input Penilaian", Icons.Rounded.TableChart, "scores", true),
        NavigationItem("Perhitungan & Hasil", Icons.Rounded.Calculate, "calculate", true)
    )

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.width(300.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(SDSSColors.Brand500, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text("🧠", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "SDSS",
                    style = SDSSTypography.titleLarge,
                    color = SDSSColors.Brand500
                )
                Text(
                    "Decision Support",
                    style = SDSSTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Menu Items
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                val isSelected = activeRoute == item.route
                val isEnabled = !item.requiresStudy || hasActiveStudy

                NavigationDrawerItem(
                    label = { 
                        Text(
                            item.label, 
                            style = SDSSTypography.titleMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.alpha(if (isEnabled) 1f else 0.5f)
                        ) 
                    },
                    selected = isSelected,
                    onClick = { if (isEnabled) onNavigate(item.route) },
                    icon = { 
                        Icon(
                            item.icon, 
                            contentDescription = null,
                            modifier = Modifier.alpha(if (isEnabled) 1f else 0.5f)
                        ) 
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = SDSSColors.Brand500,
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }

        // Footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "LOGGED IN AS",
                        style = SDSSTypography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "admin@sdss.com",
                        style = SDSSTypography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.Rounded.Logout, contentDescription = "Logout", tint = SDSSColors.Error)
                }
            }
        }
    }
}
