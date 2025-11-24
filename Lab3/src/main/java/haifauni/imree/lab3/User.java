//Submitted by Imree Cohen 312359284 and Eyal Shtienmetz 314884834

package haifauni.imree.lab3;
import java.util.regex.Pattern;

public class User {
    //Constants
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 12;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9%+_.-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*)(_+])[a-zA-Z\\d!@#$%^&*)(_+)]+$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    //Fields
    private String name;
    private String password;

    private int failedAttempts = 0;
    private boolean isBlocked = false;

    //Constructor
    public User(String name, String password) throws Exception{
        validName(name);
        validPassword(password);
        this.name = name;
        this.password = password;
    }

    //Methods
    private void validName(String name) throws Exception{
        if (name.length() > MAX_USERNAME_LENGTH){
            throw new Exception("Username is too long, try something shorter");
        }
        if (!EMAIL_PATTERN.matcher(name).matches())
            throw new Exception("Please enter a valid Email as username");
    }

    private void validPassword(String password) throws Exception {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH)
            throw new Exception("Your password is too short, add more characters");
        if (password.length() > MAX_PASSWORD_LENGTH)
            throw new Exception("Your password is too long, try a shorter one");
        if (!PASSWORD_PATTERN.matcher(password).matches())
            throw new Exception("Please enter a valid password");
    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public synchronized void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public synchronized void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public synchronized boolean isBlocked() {
        return isBlocked;
    }

    public synchronized void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        return this.name + " " + this.password;
    }
}
