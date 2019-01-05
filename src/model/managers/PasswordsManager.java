package model.managers;

import database.DatabaseManager;
import model.Employee;
import model.exceptions.ErrorMessageException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PasswordsManager {
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public static String getSalt(int length) {
        StringBuilder salt = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            salt.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(salt);
    }

    public static byte[] hash(char[] password, byte[] salt) throws ErrorMessageException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return keyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ErrorMessageException("Error while hashing a password: " + e.getMessage());
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) throws ErrorMessageException {
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }

    public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) throws ErrorMessageException {
    // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
    // Check if two passwords are equal
        return newSecurePassword.equalsIgnoreCase(securedPassword);
    }

    public static void loginEmployee(String login, String providedPassword) throws ErrorMessageException {

        Employee employee = DatabaseManager.getEmployeeByLogin(login);
        if(employee == null)
            throw new ErrorMessageException("Błędny login bądz hasło.");
        Employee employee2 = DatabaseManager.getLoggedEmployee();
        if (employee2 != null)
            logoutEmployee(employee2);
        // Encrypted and Base64 encoded password read from database
        String securePassword = DatabaseManager.getPassword(login);
        // Salt read from database
        String salt = DatabaseManager.getSalt(login);

        if (login == null)
            throw new ErrorMessageException("Błędny login bądz hasło.");
        if (providedPassword == null)
            throw new ErrorMessageException("Błędny login bądz hasło.");
        if (securePassword == null)
            throw new ErrorMessageException("Błędny login bądz hasło.");
        if (salt == null)
            throw new ErrorMessageException("Błędny login bądz hasło.");
        if (PasswordsManager.verifyUserPassword(providedPassword, securePassword, salt)){
            DatabaseManager.setEmployeeActive(employee.getUUID());
        }
        else
            throw new ErrorMessageException("Błędny login bądz hasło.");
    }

    public static void logoutEmployee(Employee employee) throws ErrorMessageException {
        DatabaseManager.setEmployeeInactive(employee.getUUID());
    }
}
