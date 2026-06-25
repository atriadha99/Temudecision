package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.RankingResult

import java.util.Locale

class AIDecisionAssistant {
    
    fun explainResult(results: List<RankingResult>, methodName: String): String {
        if (results.isEmpty()) return "Belum ada data untuk dianalisis."
        
        val best = results.first()
        return """
            Berdasarkan perhitungan menggunakan metode $methodName, alternatif terbaik adalah **${best.alternativeName}** dengan skor **${String.format(Locale.getDefault(), "%.4f", best.score)}**.
            
            ${best.alternativeName} menempati peringkat pertama karena memiliki kombinasi nilai kriteria yang paling optimal setelah dilakukan proses normalisasi dan pembobotan.
            
            Saran: Anda dapat mencoba metode lain seperti TOPSIS atau WP untuk membandingkan apakah hasilnya konsisten.
        """.trimIndent()
    }
    
    fun suggestWeights(categoryName: String): Map<String, Double> {
        return when (categoryName) {
            "Makanan" -> mapOf("Harga" to 0.3, "Rasa" to 0.4, "Porsi" to 0.2, "Rating" to 0.1)
            "Gadget" -> mapOf("Harga" to 0.2, "Spesifikasi" to 0.4, "Kamera" to 0.2, "Baterai" to 0.2)
            else -> mapOf("Kriteria 1" to 0.5, "Kriteria 2" to 0.5)
        }
    }
}
