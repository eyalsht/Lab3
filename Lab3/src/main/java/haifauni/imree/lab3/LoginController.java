//Submitted by Imree Cohen 312359284 and Eyal Shtienmetz 314884834
package haifauni.imree.lab3;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

public class LoginController {

    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;

    @FXML
    void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        ArrayList<User> users = LoginApplication.getValidUsers();

        // Find User
        User foundUser = null;
        for (User user : users) {
            if (user.getName().equals(username)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            errorLabel.setText("user or password do not match");
            return;
        }

        // Choose which Thread Task to run
        if (foundUser.getPassword().equals(password)) {
            // PASSWORD CORRECT -> Use the BlockCheckTask class
            BlockCheckTask task = new BlockCheckTask(foundUser);
            new Thread(task).start();
        } else {
            // PASSWORD WRONG -> Use the FailedLoginTask class
            FailedLoginTask task = new FailedLoginTask(foundUser);
            new Thread(task).start();
        }
    }

    // Helper methods, UI updates must stay in the Controller

    private void updateErrorLabel(String message) {
        Platform.runLater(() -> errorLabel.setText(message));
    }

    // Opens the Welcome Screen with the given username (now will be called by the thread tasks)
    private void openWelcomeScreen(String username) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("welcome-view.fxml"));
                Scene scene = new Scene(loader.load());

                WelcomeController controller = loader.getController();
                controller.setWelcomeMessage(username);

                Stage newStage = new Stage();
                newStage.setTitle("Welcome");
                newStage.setScene(scene);
                newStage.setOnCloseRequest(e -> Platform.exit());

                ((Stage) loginButton.getScene().getWindow()).close();
                newStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Thread Task 1: Checks if a user is blocked upon successful login.
     */
    private class BlockCheckTask implements Runnable {
        private final User user;

        public BlockCheckTask(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            // isBlocked is synchronized in User.java
            if (user.isBlocked()) {
                updateErrorLabel(user.getName() + " is blocked! Try again later.");
            } else {
                // resetFailedAttempts is synchronized in User.java
                user.resetFailedAttempts();
                openWelcomeScreen(user.getName());
            }
        }
    }

    /**
     * Thread Task 2: Updates failed attempts and handles blocking logic.
     */
    private class FailedLoginTask implements Runnable {
        private final User user;

        public FailedLoginTask(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            user.incrementFailedAttempts(); // Synchronized in User.java
            int maxAttempts = LoginApplication.getMaxAttempts();
            int currentAttempts = user.getFailedAttempts();

            if (currentAttempts >= maxAttempts) {
                // BLOCK THE USER
                user.setBlocked(true);
                int blockTime = LoginApplication.getBlockTimeSeconds();
                updateErrorLabel("Max attempts reached! " + user.getName() + " is blocked for " + blockTime + "s.");

                try {
                    // Wait for the duration
                    Thread.sleep(blockTime * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // UNBLOCK
                user.setBlocked(false);
                user.resetFailedAttempts();
                updateErrorLabel("Block for " + user.getName() + " lifted. You may try again.");
            } else {
                // Just a wrong attempt, not blocked yet
                updateErrorLabel("Wrong password. Attempt " + currentAttempts + " of " + maxAttempts);
            }
        }
    }
}