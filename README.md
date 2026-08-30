# 🎮 Game Arena

A console-based Java game project developed for a Programming Fundamentals (PF) course.

## Project Overview

Game Arena contains two games and an exit option:

1. **Word Scramble**
2. **Connect Four**
3. **Exit**

After a game ends, the player is returned to the main Game Arena menu.

## Word Scramble

The player is given scrambled words and has to guess the original words.

### Features

- Loads words from `words.txt`
- Randomly selects words
- Shuffles characters to create scrambled words
- Uses a **1-minute round**
- Gives the player a maximum of **10 seconds per guess**
- Awards 1 point for each correct answer
- Tracks score and attempted words
- Saves player name, best score, and date/time
- Keeps only the best score for each player
- Displays a scoreboard
- Handles empty input and file errors

## Connect Four

A two-player console version of Connect Four.

### Features

- 6 rows × 7 columns
- Two players
- Player 1 uses `R`
- Player 2 uses `Y`
- Players choose columns 1–7
- Disks fall to the lowest available position
- Detects horizontal, vertical, and diagonal wins
- Detects a draw
- Validates column input and full columns

## Project Structure

```text
GameArena/
├── GameArena.java
├── InputManager.java
├── WordScramble.java
├── ConnectFour.java
├── words.txt
├── scores.txt
├── README.md
└── .gitignore
```

## Requirements

- Java JDK
- A Java compiler
- Any Java IDE or terminal

## How to Run

Open the terminal in the project folder.

Compile:

```bash
javac GameArena.java InputManager.java WordScramble.java ConnectFour.java
```

Run:

```bash
java GameArena
```

Keep `words.txt` in the same folder as the Java files.

## Input Validation & Exception Handling

The program handles:

- Empty input
- Invalid menu choices
- Non-numeric input
- Invalid Connect Four columns
- Full Connect Four columns
- Missing word files
- Empty word files
- File reading errors
- File writing errors
- Invalid entries in the score file

## PF Concepts Used

- Classes and objects
- Methods
- Loops
- If/switch statements
- Arrays
- Strings
- Randomization
- File handling
- Exception handling
- Input validation
- Collections
- Date and time
- Basic multithreading for timed input

## Course Project

**Project:** Game Arena  
**Language:** Java  
**Type:** Console Application  
**Course:** Programming Fundamentals
