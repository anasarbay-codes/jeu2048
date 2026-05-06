import javax.swing.JOptionPane;

public class Main {
    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void runSafely(ThrowingRunnable action) {
        try {
            action.run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        runSafely(LaunchGame::launch);
    }
}
