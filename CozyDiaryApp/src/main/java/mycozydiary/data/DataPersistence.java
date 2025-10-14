package mycozydiary.data;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataPersistence {
    private static final String DATA_FILE = "diary_data.ser";

    public static boolean saveData(Map<LocalDate, DiaryEntry> entries,
                                   Map<LocalDate, String> moods,
                                   Map<String, List<LocalDate>> habits,
                                   PinManagement pinManagement) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(entries);
            oos.writeObject(moods);
            oos.writeObject(habits);
            oos.writeObject(pinManagement.getHashedPin());
            oos.writeInt(pinManagement.getFailedAttemptsCount());
            oos.writeLong(pinManagement.getLockoutTime());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static DataContainer loadData() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return new DataContainer();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Map<LocalDate, DiaryEntry> entries = (Map<LocalDate, DiaryEntry>) ois.readObject();
            Map<LocalDate, String> moods = (Map<LocalDate, String>) ois.readObject();
            Map<String, List<LocalDate>> habits = (Map<String, List<LocalDate>>) ois.readObject();

            PinManagement pinManagement = new PinManagement();
            String hashedPin = (String) ois.readObject();
            int failedAttempts = ois.readInt();
            long lockoutTime = ois.readLong();

            pinManagement.setHashedPin(hashedPin);
            pinManagement.setFailedAttempts(failedAttempts);
            pinManagement.setLockoutTime(lockoutTime);

            return new DataContainer(entries, moods, habits, pinManagement);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new DataContainer();
        }
    }

    public static boolean dataFileExists() {
        return new File(DATA_FILE).exists();
    }

    public static boolean deleteDataFile() {
        File file = new File(DATA_FILE);
        return file.exists() && file.delete();
    }

    public static class DataContainer {
        public Map<LocalDate, DiaryEntry> entries;
        public Map<LocalDate, String> moods;
        public Map<String, List<LocalDate>> habits;
        public PinManagement pinManagement;

        public DataContainer() {
            this.entries = new HashMap<>();
            this.moods = new HashMap<>();
            this.habits = new HashMap<>();
            this.pinManagement = new PinManagement();
            initializeHabits();
        }

        public DataContainer(Map<LocalDate, DiaryEntry> entries,
                             Map<LocalDate, String> moods,
                             Map<String, List<LocalDate>> habits,
                             PinManagement pinManagement) {
            this.entries = entries != null ? entries : new HashMap<>();
            this.moods = moods != null ? moods : new HashMap<>();
            this.habits = habits != null ? habits : new HashMap<>();
            this.pinManagement = pinManagement != null ? pinManagement : new PinManagement();
            initializeHabits();
        }

        private void initializeHabits() {
            if (habits == null) {
                habits = new HashMap<>();
            }
            habits.putIfAbsent("Skincare", new ArrayList<>());
            habits.putIfAbsent("Reading", new ArrayList<>());
            habits.putIfAbsent("Hydration", new ArrayList<>());
            habits.putIfAbsent("Meditation", new ArrayList<>());
        }
    }
}