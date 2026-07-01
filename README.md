# SmartChoice (TemuDecision) 🚀

**SmartChoice** adalah aplikasi Android Sistem Pendukung Keputusan (SPK) universal yang dirancang untuk membantu pengguna menentukan pilihan terbaik dari berbagai alternatif berdasarkan kriteria tertentu. Aplikasi ini mengimplementasikan berbagai metode SPK populer dalam satu platform yang modern dan responsif.

## ✨ Fitur Utama

- **Multi-Metode SPK**: Implementasi lengkap algoritma SAW, WP, TOPSIS, MOORA, SMART, dan Profile Matching.
- **Penentuan Bobot AHP**: Menggunakan metode *Analytic Hierarchy Process* untuk menghasilkan bobot kriteria yang konsisten.
- **Kategori Dinamis**: Pengguna dapat membuat kategori keputusan sendiri (misal: Memilih Kost, Memilih Laptop, dll) atau menggunakan kategori bawaan.
- **AI Decision Assistant**: Memberikan penjelasan cerdas mengapa suatu alternatif terpilih sebagai peringkat pertama.
- **Offline First**: Menggunakan **Room Database** untuk menyimpan semua data secara lokal tanpa perlu koneksi internet.
- **UI Modern**: Dibangun dengan **Jetpack Compose** dan **Material Design 3** yang mendukung Dark/Light Mode.
- **Arsitektur MVVM**: Mengikuti standar *Clean Architecture* untuk kode yang modular dan mudah dikembangkan.

## 📊 Metode SPK yang Tersedia

1.  **AHP (Analytic Hierarchy Process)**: Digunakan untuk pembobotan kriteria melalui perbandingan berpasangan.
2.  **SAW (Simple Additive Weighting)**: Metode penjumlahan terbobot.
3.  **WP (Weighted Product)**: Menggunakan perkalian untuk menghubungkan rating atribut.
4.  **TOPSIS**: Mencari solusi berdasarkan jarak terdekat dengan solusi ideal positif.
5.  **MOORA**: Optimasi multi-objektif berdasarkan rasio analisis.
6.  **SMART**: Teknik pengambilan keputusan multi-atribut sederhana.
7.  **Profile Matching**: Menilai berdasarkan kompetensi/target yang diinginkan (gap analysis).

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material Design 3
- **Dependency Injection**: Hilt
- **Local Database**: Room
- **Async Processing**: Coroutines & Flow
- **Navigation**: Navigation Compose
- **Preference Storage**: DataStore

## 📂 Struktur Proyek

```text
app/src/main/java/com/andika/temudecision/
├── data/               # Layer Data (Entity, DAO, Database, Repository)
├── domain/             # Layer Bisnis (Model, Algoritma SPK, Assistant)
│   └── methods/        # Implementasi Logika Algoritma (SAW, TOPSIS, dll)
├── presentation/       # Layer UI (Compose Screens, ViewModels, Theme)
│   ├── navigation/     # Konfigurasi Navigasi
│   ├── screen/         # UI Screen (Dashboard, Detail, dll)
│   └── viewmodel/      # Manajemen State UI
├── di/                 # Konfigurasi Dependency Injection (Hilt)
└── utils/              # Helper & Data Seeding
```

## 🚀 Cara Menjalankan

1.  Clone repository ini atau buka folder proyek di **Android Studio (Ladybug atau versi terbaru)**.
2.  Pastikan SDK Android 35 sudah terinstall.
3.  Lakukan **Gradle Sync** untuk mengunduh library yang diperlukan.
4.  Klik **Run** untuk menginstal aplikasi di Emulator atau Perangkat Fisik.

## 📝 Catatan Pengembangan
Aplikasi ini dirancang untuk kebutuhan akademik (Skripsi/Tugas Akhir) maupun penggunaan praktis sehari-hari. Struktur kode dibuat sangat modular sehingga penambahan metode SPK baru dapat dilakukan dengan mudah tanpa merusak logika yang sudah ada.

---
