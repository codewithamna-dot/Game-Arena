public class GameArena {
    public static void main(String[] args) {
        WordScramble wordScramble = new WordScramble();
        ConnectFour connectFour = new ConnectFour();

        System.out.println("=================================");
        System.out.println("        WELCOME TO GAME ARENA");
        System.out.println("=================================");

        boolean running = true;

        while (running) {
            System.out.println("\n----------- MAIN MENU -----------");
            System.out.println("1. Word Scramble");
            System.out.println("2. Connect Four");
            System.out.println("3. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    wordScramble.showMenu();
                    break;
                case 2:
                    connectFour.play();
                    break;
                case 3:
                    running = false;
                    System.out.println("\nThank you for playing Game Arena!");
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        InputManager.close();
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = InputManager.readLine();

            if (input == null) {
                System.out.println("\nInput ended. Exiting Game Arena.");
                System.exit(0);
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
}
