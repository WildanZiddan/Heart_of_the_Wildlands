# 🌲 Heart of the Wildlands

> **2D Adventure RPG** berbasis Java & JavaFX — Jelajahi dunia, tingkatkan skill, dan kalahkan boss yang menghalangi jalanmu!

---

## ✨ Fitur Utama

| Fitur | Deskripsi |
| :--- | :--- |
| ⚔️ **Dynamic Combat & Bosses** | Hadapi musuh bervariasi, termasuk tantangan besar seperti *Slime Boss* |
| 🌳 **Skill Tree System** | Kustomisasi progres karakter dengan pohon skill yang interaktif |
| 📜 **Quest System** | Selesaikan berbagai misi untuk mendapatkan reward dan progres cerita |
| 🎨 **FXML Powered UI** | Antarmuka bersih dan terorganisir menggunakan JavaFX Scene Builder |
| 🏗️ **OOP Architecture** | Struktur kode solid menggunakan prinsip *Object-Oriented Programming* |

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
| :--- | :--- |
| **Bahasa Pemrograman** | Java |
| **UI Framework** | JavaFX & FXML |
| **IDE** | IntelliJ IDEA |
| **Version Control** | Git & GitHub |

---

## 🚀 Cara Menjalankan

### Prerequisites
- JDK 11 atau lebih baru
- JavaFX SDK
- IntelliJ IDEA (recommended)

### Langkah-langkah

**1. Clone repositori**
```bash
git clone https://github.com/WildanYazid/Heart_of_the_Wildlands.git
cd Heart_of_the_Wildlands
```

**2. Buka di IntelliJ IDEA**
- Pilih `File → Open` dan arahkan ke folder project
- Pastikan SDK Java sudah terkonfigurasi di `Project Structure`

**3. Konfigurasi JavaFX**
- Tambahkan library JavaFX di `Project Structure → Libraries`
- Tambahkan VM options berikut di Run Configuration:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

**4. Jalankan project**
- Buka `Main.java` atau entry point utama
- Klik kanan → **Run**

---

## 📂 Struktur Project

```
Heart_of_the_Wildlands/
├── src/
│   ├── main/
│   │   ├── Main.java              # Entry point utama
│   │   ├── GameController.java    # Logika utama jalannya game
│   │   ├── SkillTree.java         # Pengaturan progres kemampuan karakter
│   │   └── Quest.java             # Sistem misi dan objektif
│   └── resources/
│       ├── game.fxml              # Layout utama arena permainan
│       └── assets/                # Sprite, sound, dan aset lainnya
└── README.md
```

---

> [!NOTE]
> Project ini dikembangkan sebagai bagian dari eksplorasi **Struktur Data** dan pengembangan game berbasis desktop.

---

<p align="center">
  Developed by <strong>Wildan Yazid Ziddan</strong>
</p>
