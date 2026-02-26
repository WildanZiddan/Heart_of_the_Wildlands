# 🌲 Heart of the Wildlands

> A **2D Adventure RPG** built with Java & JavaFX — Explore the world, level up your skills, and defeat powerful bosses standing in your way!

---

## ✨ Features

| Feature | Description |
| :--- | :--- |
| ⚔️ **Dynamic Combat & Bosses** | Face a variety of enemies, including challenging bosses like the *Slime Boss* |
| 🌳 **Skill Tree System** | Customize your character's progression with an interactive skill tree |
| 📜 **Quest System** | Complete various missions to earn rewards and advance the story |
| 🎨 **FXML Powered UI** | Clean and organized interface built with JavaFX Scene Builder |
| 🏗️ **OOP Architecture** | Solid code structure following *Object-Oriented Programming* principles |

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Programming Language** | Java |
| **UI Framework** | JavaFX & FXML |
| **IDE** | IntelliJ IDEA |
| **Version Control** | Git & GitHub |

---

## 🚀 Getting Started

### Prerequisites
- JDK 11 or higher
- JavaFX SDK
- IntelliJ IDEA (recommended)

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/WildanYazid/Heart_of_the_Wildlands.git
cd Heart_of_the_Wildlands
```

**2. Open in IntelliJ IDEA**
- Go to `File → Open` and navigate to the project folder
- Make sure the Java SDK is configured under `Project Structure`

**3. Configure JavaFX**
- Add the JavaFX library under `Project Structure → Libraries`
- Add the following VM options in your Run Configuration:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

**4. Run the project**
- Open `Main.java` or the main entry point
- Right-click → **Run**

---

## 📂 Project Structure

```
Heart_of_the_Wildlands/
├── src/
│   ├── main/
│   │   ├── Main.java              # Main entry point
│   │   ├── GameController.java    # Core game logic
│   │   ├── SkillTree.java         # Character skill progression
│   │   └── Quest.java             # Mission and objective system
│   └── resources/
│       ├── game.fxml              # Main game arena layout
│       └── assets/                # Sprites, sounds, and other assets
└── README.md
```

---

> [!NOTE]
> This project was developed as part of an exploration in **Data Structures** and desktop-based game development.

---

<p align="center">
  Developed by <strong>Wildan Yazid Ziddan</strong>
</p>
