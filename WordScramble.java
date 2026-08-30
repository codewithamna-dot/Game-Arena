import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WordScramble {
    private static final String WORD_FILE = "words.txt";
    private static final String SCORE_FILE = "scores.txt";

    private static final long ROUND_TIME_MS = 60_000;
    private static final long GUESS_TIME_MS = 10_000;

    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public WordScramble() {
        loadWords();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n======= WORD SCRAMBLE =======");
            System.out.println("1. Start New Game");
            System.out.println("2. Display Score Board");
            System.out.println("3. Back to Game Arena");

            int choice = readMenuChoice();

            switch (choice) {
                case 1:
                    startGame();
                    break;
                case 2:
                    displayScoreBoard();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    private void startGame() {
        if (words.isEmpty()) {
            System.out.println("No words are available. Please check " + WORD_FILE + ".");
            return;
        }

        System.out.print("\nEnter player name: ");
        String playerName = readLine();

        while (playerName == null || playerName.trim().isEmpty()) {
            System.out.println("Player name cannot be empty.");
            System.out.print("Enter player name: ");
            playerName = readLine();
        }

        playerName = playerName.trim();

        int score = 0;
        int attempted = 0;

        System.out.println("\nGet ready!");
        System.out.println("You have 1 minute for the round.");
        System.out.println("You have 10 seconds to answer each word.");
        System.out.println("Type your answer and press Enter.");

        long roundEnd = System.currentTimeMillis() + ROUND_TIME_MS;

        while (System.currentTimeMillis() < roundEnd) {
            String original = words.get(random.nextInt(words.size()));
            String scrambled = scramble(original);

            long remainingRoundTime = roundEnd - System.currentTimeMillis();

            if (remainingRoundTime <= 0) {
                break;
            }

            System.out.println("\nScrambled word: " + scrambled);
            System.out.println("Time left in round: " + (remainingRoundTime / 1000) + " seconds");
            System.out.print("Your guess: ");

            String guess = InputManager.readLine(
                    Math.min(GUESS_TIME_MS, remainingRoundTime)
            );

            if (guess == null) {
                if (System.currentTimeMillis() >= roundEnd) {
                    System.out.println("\nThe 1-minute round is over.");
                    break;
                }

                attempted++;
                System.out.println("\nTime's up for this word!");
                System.out.println("Correct word: " + original);
            } else {
                attempted++;

                if (guess.trim().equalsIgnoreCase(original)) {
                    score++;
                    System.out.println("Correct! +1 point.");
                } else {
                    System.out.println("Incorrect.");
                    System.out.println("Correct word: " + original);
                }
            }
        }

        System.out.println("\n========== ROUND OVER ==========");
        System.out.println("Player: " + playerName);
        System.out.println("Score: " + score);
        System.out.println("Words attempted: " + attempted);

        saveBestScore(playerName, score);
    }

    private void loadWords() {
        words.clear();

        File file = new File(WORD_FILE);

        if (!file.exists()) {
            System.out.println("Warning: " + WORD_FILE + " was not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String word = line.trim();

                if (!word.isEmpty()) {
                    words.add(word);
                }
            }

            if (words.isEmpty()) {
                System.out.println("Warning: " + WORD_FILE + " is empty.");
            }
        } catch (IOException e) {
            System.out.println("Could not read " + WORD_FILE + ".");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String scramble(String word) {
        if (word.length() <= 1) {
            return word;
        }

        boolean allSame = true;

        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) != word.charAt(0)) {
                allSame = false;
                break;
            }
        }

        if (allSame) {
            return word;
        }

        List<Character> characters = new ArrayList<>();

        for (char c : word.toCharArray()) {
            characters.add(c);
        }

        String scrambled;

        do {
            Collections.shuffle(characters, random);

            StringBuilder result = new StringBuilder();

            for (char c : characters) {
                result.append(c);
            }

            scrambled = result.toString();
        } while (scrambled.equals(word));

        return scrambled;
    }

    private void saveBestScore(String playerName, int newScore) {
        Map<String, ScoreRecord> scores = new LinkedHashMap<>();
        File file = new File(SCORE_FILE);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length == 3) {
                        try {
                            String name = parts[0];
                            int score = Integer.parseInt(parts[1]);
                            String dateTime = parts[2];

                            scores.put(name,
                                    new ScoreRecord(name, score, dateTime));
                        } catch (NumberFormatException ignored) {
                            // Ignore malformed score entries.
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not read the score file.");
            }
        }

        ScoreRecord oldRecord = scores.get(playerName);

        if (oldRecord == null || newScore > oldRecord.score) {
            String dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            scores.put(playerName,
                    new ScoreRecord(playerName, newScore, dateTime));

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (ScoreRecord record : scores.values()) {
                    writer.println(record.name + "|" + record.score + "|"
                            + record.dateTime);
                }

                if (oldRecord == null) {
                    System.out.println("Best score saved.");
                } else {
                    System.out.println("New best score! Score file updated.");
                }
            } catch (IOException e) {
                System.out.println("Could not save the score.");
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Your previous best score was "
                    + oldRecord.score + ". The score file was not changed.");
        }
    }

    private void displayScoreBoard() {
        File file = new File(SCORE_FILE);

        if (!file.exists()) {
            System.out.println("\nNo scores have been recorded yet.");
            return;
        }

        List<ScoreRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                if (parts.length == 3) {
                    try {
                        records.add(new ScoreRecord(
                                parts[0],
                                Integer.parseInt(parts[1]),
                                parts[2]
                        ));
                    } catch (NumberFormatException ignored) {
                        // Ignore invalid entries.
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read the score file.");
            return;
        }

        if (records.isEmpty()) {
            System.out.println("\nNo valid scores are available.");
            return;
        }

        records.sort((a, b) -> Integer.compare(b.score, a.score));

        System.out.println("\n========== SCORE BOARD ==========");
        System.out.printf("%-20s %-10s %-20s%n",
                "Player", "Score", "Date & Time");
        System.out.println("------------------------------------------------------------");

        for (ScoreRecord record : records) {
            System.out.printf("%-20s %-10d %-20s%n",
                    record.name, record.score, record.dateTime);
        }
    }

    private int readMenuChoice() {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = readLine();

            if (input == null) {
                return 3;
            }

            input = input.trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readLine() {
        return InputManager.readLine();
    }

    private static class ScoreRecord {
        String name;
        int score;
        String dateTime;

        ScoreRecord(String name, int score, String dateTime) {
            this.name = name;
            this.score = score;
            this.dateTime = dateTime;
        }
    }
}
