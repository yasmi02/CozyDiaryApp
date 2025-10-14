package mycozydiary.data;

import java.time.LocalDate;
import java.util.*;

public class DataManager {
    private Map<LocalDate, DiaryEntry> entries;
    private Map<LocalDate, String> moods;
    private Map<String, List<LocalDate>> habits;
    private PinManagement pinManagement;

    public DataManager() {
        DataPersistence.DataContainer data = DataPersistence.loadData();
        this.entries = data.entries;
        this.moods = data.moods;
        this.habits = data.habits;
        this.pinManagement = data.pinManagement;
    }

    private boolean saveAll() {
        return DataPersistence.saveData(entries, moods, habits, pinManagement);
    }

    // ==================== Entry Management ====================

    public void addEntry(LocalDate date, DiaryEntry entry) {
        if (date == null || entry == null) {
            throw new IllegalArgumentException("Date and entry cannot be null");
        }
        entries.put(date, entry);
        saveAll();
    }

    public DiaryEntry getEntry(LocalDate date) {
        if (date == null) {
            return null;
        }
        return entries.get(date);
    }

    public Map<LocalDate, DiaryEntry> getAllEntries() {
        return new HashMap<>(entries);
    }

    public boolean hasEntry(LocalDate date) {
        if (date == null) {
            return false;
        }
        return entries.containsKey(date);
    }

    public void deleteEntry(LocalDate date) {
        if (date != null && entries.containsKey(date)) {
            entries.remove(date);
            saveAll();
        }
    }

    public int getTotalEntries() {
        return entries.size();
    }

    // ==================== Mood Management ====================

    public void setMood(LocalDate date, String mood) {
        if (date == null || mood == null) {
            return;
        }
        moods.put(date, mood);
        saveAll();
    }

    public String getMood(LocalDate date) {
        if (date == null) {
            return null;
        }
        return moods.get(date);
    }

    public Map<LocalDate, String> getAllMoods() {
        return new HashMap<>(moods);
    }

    // ==================== Habit Management ====================

    public void toggleHabit(String habitName, LocalDate date) {
        if (habitName == null || date == null || !habits.containsKey(habitName)) {
            return;
        }

        List<LocalDate> dates = habits.get(habitName);
        if (dates.contains(date)) {
            dates.remove(date);
        } else {
            dates.add(date);
        }
        saveAll();
    }

    public boolean isHabitCompleted(String habitName, LocalDate date) {
        if (habitName == null || date == null || !habits.containsKey(habitName)) {
            return false;
        }
        return habits.get(habitName).contains(date);
    }

    public int getHabitProgress(String habitName) {
        if (habitName == null || !habits.containsKey(habitName)) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        int daysInMonth = today.lengthOfMonth();
        int completedDays = 0;

        List<LocalDate> habitDates = habits.get(habitName);
        if (habitDates == null) {
            return 0;
        }

        for (LocalDate date : habitDates) {
            if (date != null &&
                    date.getMonth() == today.getMonth() &&
                    date.getYear() == today.getYear()) {
                completedDays++;
            }
        }

        return (completedDays * 100) / daysInMonth;
    }

    public Map<String, List<LocalDate>> getHabits() {
        return habits;
    }

    public List<String> getHabitNames() {
        return new ArrayList<>(habits.keySet());
    }

    // ==================== PIN Management ====================

    public boolean verifyPin(String inputPin) {
        return pinManagement.verifyPin(inputPin);
    }

    public boolean changePin(String oldPin, String newPin) {
        boolean success = pinManagement.changePin(oldPin, newPin);
        if (success) {
            saveAll();
        }
        return success;
    }

    public boolean isLockedOut() {
        return pinManagement.isLockedOut();
    }

    public int getRemainingAttempts() {
        return pinManagement.getRemainingAttempts();
    }

    public int getRemainingLockoutTime() {
        return pinManagement.getRemainingLockoutTime();
    }

    public boolean isUsingDefaultPin() {
        return pinManagement.isUsingDefaultPin();
    }

    public PinManagement getPinManagement() {
        return pinManagement;
    }

    public boolean forceChangePin(String newPin) {
        boolean success = pinManagement.forceChangePin(newPin);
        if (success) {
            saveAll();
        }
        return success;
    }

    // ==================== Utility Methods ====================

    public void clearAllData() {
        entries.clear();
        moods.clear();

        // Reinitialize habits
        habits.clear();
        habits.put("Skincare", new ArrayList<>());
        habits.put("Reading", new ArrayList<>());
        habits.put("Hydration", new ArrayList<>());
        habits.put("Meditation", new ArrayList<>());

        pinManagement.resetToDefault();
        saveAll();
    }

    public boolean hasDataFile() {
        return DataPersistence.dataFileExists();
    }

    public boolean exportData(String filename) {
        // Future enhancement: export to JSON or CSV
        return false;
    }

    public boolean importData(String filename) {
        // Future enhancement: import from JSON or CSV
        return false;
    }
}