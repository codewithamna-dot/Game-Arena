public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private final char[][] board = new char[ROWS][COLUMNS];

    public void play() {
        initializeBoard();

        System.out.println("\n=================================");
        System.out.println("          CONNECT FOUR");
        System.out.println("=================================");
        System.out.println("Player 1 = R (Red)");
        System.out.println("Player 2 = Y (Yellow)");
        System.out.println("Connect four disks horizontally, vertically, or diagonally.");
        System.out.println("Enter a column number from 1 to 7.");

        char currentPlayer = 'R';

        while (true) {
            displayBoard();

            int column = getColumn(currentPlayer);
            int row = dropDisk(column, currentPlayer);

            if (row == -1) {
                System.out.println("That column is full. Choose another column.");
                continue;
            }

            if (hasWon(row, column, currentPlayer)) {
                displayBoard();
                System.out.println("Player " + playerNumber(currentPlayer)
                        + " (" + currentPlayer + ") wins!");
                break;
            }

            if (isBoardFull()) {
                displayBoard();
                System.out.println("The game is a draw!");
                break;
            }

            currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';
        }

        System.out.println("Returning to Game Arena menu...");
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                board[row][column] = '.';
            }
        }
    }

    private void displayBoard() {
        System.out.println();

        for (int row = 0; row < ROWS; row++) {
            System.out.print("| ");

            for (int column = 0; column < COLUMNS; column++) {
                System.out.print(board[row][column] + " ");
            }

            System.out.println("|");
        }

        System.out.println("-----------------");
        System.out.println("  1 2 3 4 5 6 7");
    }

    private int getColumn(char player) {
        while (true) {
            System.out.print("Player " + playerNumber(player)
                    + " (" + player + "), choose a column: ");

            String input = InputManager.readLine();

            if (input == null) {
                System.out.println("\nInput ended. Returning to menu.");
                return 0;
            }

            input = input.trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }

            try {
                int column = Integer.parseInt(input);

                if (column < 1 || column > 7) {
                    System.out.println("Please enter a column between 1 and 7.");
                } else {
                    return column - 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid column number.");
            }
        }
    }

    private int dropDisk(int column, char player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == '.') {
                board[row][column] = player;
                return row;
            }
        }

        return -1;
    }

    private boolean hasWon(int row, int column, char player) {
        return countConnected(row, column, 0, 1, player)
                    + countConnected(row, column, 0, -1, player) - 1 >= 4
                || countConnected(row, column, 1, 0, player) >= 4
                || countConnected(row, column, 1, 1, player)
                    + countConnected(row, column, -1, -1, player) - 1 >= 4
                || countConnected(row, column, 1, -1, player)
                    + countConnected(row, column, -1, 1, player) - 1 >= 4;
    }

    private int countConnected(int row, int column, int rowDirection,
                               int columnDirection, char player) {
        int count = 0;
        int currentRow = row;
        int currentColumn = column;

        while (currentRow >= 0 && currentRow < ROWS
                && currentColumn >= 0 && currentColumn < COLUMNS
                && board[currentRow][currentColumn] == player) {

            count++;
            currentRow += rowDirection;
            currentColumn += columnDirection;
        }

        return count;
    }

    private boolean isBoardFull() {
        for (int column = 0; column < COLUMNS; column++) {
            if (board[0][column] == '.') {
                return false;
            }
        }

        return true;
    }

    private int playerNumber(char player) {
        return player == 'R' ? 1 : 2;
    }
}
