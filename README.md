# SuperMarioRun_Part2

An Android-based endless runner game inspired by classic Mario mechanics.  
In this game, the player (Mario) must avoid falling shells and collect coins to increase their score.

---

## 📱 Features

- 🏃 Endless side-scrolling gameplay with increasing difficulty
- 💣 Random falling bombs (green shell / red shell)
- 🪙 Coin collection system
- ❤️ Life system – lose lives upon collision
- 🎮 Tilt-based control (optional)
- 📍 Location fetching and integration
- 🏆 High score tracking with persistent storage
- 🗺️ Google Maps integration to show where a score was recorded

---

## 🧰 Technologies Used

- Kotlin
- Android SDK
- CountDownTimer for object spawning
- SensorManager for tilt detection
- RecyclerView for displaying high scores
- Google Maps API

---

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SuperMarioRun_Part2.git
   ```

2. Open with **Android Studio Arctic Fox or later**

3. Replace the ` android:value=` on the `AndroidManifest.xml` with your own Google Maps API key

4. Build & run the project on a real Android device (tilt mode needs physical sensors)

---

## 📂 Project Structure

```
S📁 SuperMarioRun_Part2/
├── 📄 .gitignore
├── 📄 build.gradle.kts
├── 📄 settings.gradle.kts
├── 📁 app/
│   ├── 📄 build.gradle.kts
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/com/example/supermariorun/
│   │   │   │   ├── 📄 MainActivity.kt
│   │   │   │   ├── 📄 GameLogic.kt
│   │   │   │   ├── 📄 SpawnerManager.kt
│   │   │   │   ├── 📁 activities/
│   │   │   │   │   ├── 📄 MenuActivity.kt
│   │   │   │   │   ├── 📄 HighScoresActivity.kt
│   │   │   │   │   ├── 📄 MapActivity.kt
│   │   │   │   ├── 📁 data/
│   │   │   │   │   ├── 📄 GameData.kt
│   │   │   │   │   ├── 📄 HighScore.kt
│   │   │   │   ├── 📁 utilities/
│   │   │   │   │   ├── 📄 TiltCallback.kt
│   │   │   │   │   ├── 📄 TiltDetector.kt
│   │   │   │   │   ├── 📄 SoundManager.kt
│   │   │   │   │   ├── 📄 SignalManager.kt
│   │   │   │   │   ├── 📄 GameTicker.kt
│   │   │   │   │   ├── 📄 UIUpdater.kt
│   │   │   │   │   ├── 📄 HighScoreManager.kt
│   │   │   │   │   ├── 📄 LocationFetcher.kt
│   │   │   ├── 📁 res/
│   │   │   │   ├── 📁 layout/
│   │   │   │   │   ├── 📄 activity_main.xml
│   │   │   │   │   ├── 📄 activity_high_scores.xml
│   │   │   │   │   ├── 📄 activity_menu.xml
│   │   │   │   │   ├── 📄 item_high_score.xml
│   │   │   │   │   ├── 📄 activity_map.xml
│   │   │   │   ├── 📁 drawable/
│   │   │   │   ├── 📁 values/
├── 📄 README.md ✅
```
## 👨‍💻 Author

Created by Offir Tura
For Android Development Course – Part 2 Submission  
Afeka Collage, 2025.
