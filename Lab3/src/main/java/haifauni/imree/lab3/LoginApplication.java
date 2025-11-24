//Submitted by Imree Cohen 312359284 and Eyal Shtienmetz 314884834
package haifauni.imree.lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Platform;

public class LoginApplication extends Application {

    private static ArrayList<User> validUsers = new ArrayList<>();

    private static int maxAttempts;
    private static int blockTimeSeconds;

    public static int getMaxAttempts() { return maxAttempts; }
    public static int getBlockTimeSeconds() { return blockTimeSeconds; }

    /**
     * Reads "users.txt", silently validates each user, and
     * adds only the *valid* ones to the list.
     * This method is called once at the start of the application.
     */
    private void loadValidUsers() {
        try {
            File usersFile = new File("Lab3/Users.txt"); // Assumes Users.txt is the input file.
            Scanner fileReader = new Scanner(usersFile);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\s+", 2);
                    if (parts.length != 2) {
                        continue; // Silently skip malformed lines
                    }

                    String name = parts[0];
                    String password = parts[1];

                    User newUser = new User(name, password);

                    // If no exception was thrown, the user is valid.
                    validUsers.add(newUser);



                } catch (Exception e) {
                    // Intentionally left blank.
                    // We only want to add valid users, so we
                    // "silently" ignore any lines that fail validation.
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Fatal Error: users.txt file not found. App cannot run.");
        }
    }

    public static ArrayList<User> getValidUsers() {
        return validUsers;
    }

    @Override
    public void start(Stage stage) throws IOException {
        java.util.List<String> args = getParameters().getRaw();

        if (args.size() < 2) {
            System.err.println("Error: Please provide 2 arguments: <Max Attempts> <Block Time>");
            Platform.exit(); // Close the app if args are missing
            return;
        }

        try {
            maxAttempts = Integer.parseInt(args.get(0));
            blockTimeSeconds = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            System.err.println("Error: Arguments must be numbers.");
            Platform.exit();
            return;
        }

        loadValidUsers(); // Load all valid users into memory before doing anything else

        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("User Login");
        stage.setScene(scene);

        // Ensures clicking 'X' on the login window quits the whole app
        stage.setOnCloseRequest(e -> Platform.exit());

        stage.show();
    }
}
