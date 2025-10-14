// ==================== FILE: src/mycozydiary/data/PinManagement.java ====================
package mycozydiary.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PinManagement {
    private String hashedPin;
    private static final String DEFAULT_PIN = "1234";
    private int failedAttempts;
    private long lockoutTime;
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 60000;

    public PinManagement() {
        this.hashedPin = hashPin(DEFAULT_PIN);
        this.failedAttempts = 0;
        this.lockoutTime = 0;
    }

    public PinManagement(String initialPin) {
        if (initialPin != null && isValidPin(initialPin)) {
            this.hashedPin = hashPin(initialPin);
        } else {
            this.hashedPin = hashPin(DEFAULT_PIN);
        }
        this.failedAttempts = 0;
        this.lockoutTime = 0;
    }

    public boolean verifyPin(String inputPin) {
        if (inputPin == null || inputPin.trim().isEmpty()) {
            return false;
        }

        if (isLockedOut()) {
            return false;
        }

        String hashedInput = hashPin(inputPin);
        boolean isCorrect = hashedPin.equals(hashedInput);

        if (isCorrect) {
            resetFailedAttempts();
        } else {
            incrementFailedAttempts();
        }

        return isCorrect;
    }

    public boolean changePin(String oldPin, String newPin) {
        if (oldPin == null || newPin == null) {
            return false;
        }

        if (!verifyPin(oldPin)) {
            return false;
        }

        if (!isValidPin(newPin)) {
            return false;
        }

        this.hashedPin = hashPin(newPin);
        resetFailedAttempts();
        return true;
    }

    public boolean forceChangePin(String newPin) {
        if (!isValidPin(newPin)) {
            return false;
        }

        this.hashedPin = hashPin(newPin);
        resetFailedAttempts();
        return true;
    }

    private boolean isValidPin(String pin) {
        if (pin == null || pin.length() != 4) {
            return false;
        }

        return pin.matches("\\d{4}");
    }

    private String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            return Base64.getEncoder().encodeToString(pin.getBytes());
        }
    }

    public boolean isLockedOut() {
        if (failedAttempts >= MAX_ATTEMPTS) {
            long currentTime = System.currentTimeMillis();
            if (currentTime < lockoutTime) {
                return true;
            } else {
                resetFailedAttempts();
                return false;
            }
        }
        return false;
    }

    public int getRemainingLockoutTime() {
        if (!isLockedOut()) {
            return 0;
        }
        long remaining = (lockoutTime - System.currentTimeMillis()) / 1000;
        return (int) Math.max(0, remaining);
    }

    private void incrementFailedAttempts() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            lockoutTime = System.currentTimeMillis() + LOCKOUT_DURATION;
        }
    }

    private void resetFailedAttempts() {
        failedAttempts = 0;
        lockoutTime = 0;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public int getRemainingAttempts() {
        return Math.max(0, MAX_ATTEMPTS - failedAttempts);
    }

    public void resetToDefault() {
        this.hashedPin = hashPin(DEFAULT_PIN);
        resetFailedAttempts();
    }

    public boolean isUsingDefaultPin() {
        return hashedPin.equals(hashPin(DEFAULT_PIN));
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public void setHashedPin(String hashedPin) {
        this.hashedPin = hashedPin;
    }

    public int getFailedAttemptsCount() {
        return failedAttempts;
    }

    public void setFailedAttempts(int attempts) {
        this.failedAttempts = attempts;
    }

    public long getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(long time) {
        this.lockoutTime = time;
    }
}