package com.andika.temudecision.utils

import com.andika.temudecision.data.dao.CategoryDao
import com.andika.temudecision.data.entity.CategoryEntity
import javax.inject.Inject

class DataSeeder @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun seed() {
        val defaultCategories = listOf(
            CategoryEntity(name = "Makanan", icon = "🍔", isDefault = true),
            CategoryEntity(name = "Minuman", icon = "☕", isDefault = true),
            CategoryEntity(name = "Gadget", icon = "📱", isDefault = true),
            CategoryEntity(name = "Fashion", icon = "👕", isDefault = true),
            CategoryEntity(name = "Skincare", icon = "🧴", isDefault = true),
            CategoryEntity(name = "Parfum", icon = "🌸", isDefault = true),
            CategoryEntity(name = "Vape", icon = "💨", isDefault = true),
            CategoryEntity(name = "Tempat Nongkrong", icon = "🍽", isDefault = true),
            CategoryEntity(name = "Transportasi", icon = "🚗", isDefault = true),
            CategoryEntity(name = "Olahraga", icon = "🏋", isDefault = true),
            CategoryEntity(name = "Gaming", icon = "🎮", isDefault = true),
            CategoryEntity(name = "Hiburan", icon = "🎬", isDefault = true),
            CategoryEntity(name = "Pendidikan", icon = "📚", isDefault = true),
            CategoryEntity(name = "Karier", icon = "💼", isDefault = true),
            CategoryEntity(name = "Travel", icon = "✈", isDefault = true),
            CategoryEntity(name = "Keuangan", icon = "💰", isDefault = true),
            CategoryEntity(name = "Lifestyle", icon = "❤️", isDefault = true),
            CategoryEntity(name = "Rumah Tangga", icon = "🏠", isDefault = true),
            CategoryEntity(name = "Hewan Peliharaan", icon = "🐱", isDefault = true),
            CategoryEntity(name = "Teknologi", icon = "💻", isDefault = true),
            CategoryEntity(name = "Produktivitas", icon = "⏰", isDefault = true)
        )
        
        defaultCategories.forEach {
            categoryDao.insertCategory(it)
        }
    }
}
