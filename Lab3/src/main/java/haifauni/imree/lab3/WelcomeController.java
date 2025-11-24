//Submitted by Imree Cohen 312359284 and Eyal Shtienmetz 314884834
package haifauni.imree.lab3;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomeController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    // public method to pass the username and set the welcome message
    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    // Closes the welcome window and re-opens the login screen.
    @FXML
    void onLogoutButtonClick(ActionEvent event) {
        try {
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Create a new stage (window) for the login screen
            Stage loginStage = new Stage();
            loginStage.setTitle("User Login");
            loginStage.setScene(scene);

            // Make sure clicking 'X' on the new login window exits the app
            loginStage.setOnCloseRequest(e -> Platform.exit());

            // Get the current (welcome) stage and close it
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            // Show the new login stage
            loginStage.show();

        } catch (IOException e) {
            // This is a fatal error, print to console
            e.printStackTrace();
        }
    }
}
