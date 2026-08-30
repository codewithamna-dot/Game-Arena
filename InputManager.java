import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class InputManager {
    private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();
    private static final BufferedReader READER =
            new BufferedReader(new InputStreamReader(System.in));

    static {
        Thread inputThread = new Thread(() -> {
            try {
                String line;
                while ((line = READER.readLine()) != null) {
                    QUEUE.put(line);
                }
            } catch (IOException | InterruptedException e) {
                // Console input has ended.
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();
    }

    private InputManager() {
    }

    public static String readLine() {
        try {
            return QUEUE.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static String readLine(long timeoutMillis) {
        try {
            return QUEUE.poll(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static void close() {
        try {
            READER.close();
        } catch (IOException ignored) {
        }
    }
}
