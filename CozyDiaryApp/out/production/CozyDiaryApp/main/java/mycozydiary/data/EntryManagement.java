// ==================== FILE: src/mycozydiary/data/EntryManagement.java ====================
package mycozydiary.data;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles entry-related operations and queries
 */
public class EntryManagement {
    private Map<LocalDate, DiaryEntry> entries;

    public EntryManagement() {
        this.entries = new HashMap<>();
    }

    // Add entry
    public void addEntry(LocalDate date, DiaryEntry entry) {
        if (date == null || entry == null) {
            throw new IllegalArgumentException("Date and entry cannot be null");
        }
        entries.put(date, entry);
    }

    // Get single entry
    public DiaryEntry getEntry(LocalDate date) {
        if (date == null) {
            return null;
        }
        return entries.get(date);
    }

    // Get all entries
    public Map<LocalDate, DiaryEntry> getAllEntries() {
        return new HashMap<>(entries);
    }

    // Check if entry exists
    public boolean hasEntry(LocalDate date) {
        if (date == null) {
            return false;
        }
        return entries.containsKey(date);
    }

    // Delete entry
    public boolean deleteEntry(LocalDate date) {
        if (date == null || !entries.containsKey(date)) {
            return false;
        }
        entries.remove(date);
        return true;
    }

    // Update entry
    public boolean updateEntry(LocalDate date, DiaryEntry newEntry) {
        if (date == null || newEntry == null || !entries.containsKey(date)) {
            return false;
        }
        entries.put(date, newEntry);
        return true;
    }

    // Get entries for a specific month
    public List<DiaryEntry> getEntriesForMonth(int year, int month) {
        return entries.entrySet().stream()
                .filter(entry -> entry.getKey().getYear() == year &&
                        entry.getKey().getMonthValue() == month)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    // Get entries for a date range
    public List<DiaryEntry> getEntriesInRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }

        return entries.entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(startDate) &&
                        !entry.getKey().isAfter(endDate))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(DiaryEntry::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    // Search entries by keyword
    public List<DiaryEntry> searchEntries(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerKeyword = keyword.toLowerCase();
        return entries.values().stream()
                .filter(entry -> entry.getTitle().toLowerCase().contains(lowerKeyword) ||
                        entry.getContent().toLowerCase().contains(lowerKeyword))
                .sorted(Comparator.comparing(DiaryEntry::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    // Get total entry count
    public int getEntryCount() {
        return entries.size();
    }

    // Get entries by mood
    public List<DiaryEntry> getEntriesByMood(String mood) {
        if (mood == null) {
            return new ArrayList<>();
        }

        return entries.values().stream()
                .filter(entry -> mood.equals(entry.getMood()))
                .sorted(Comparator.comparing(DiaryEntry::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    // Clear all entries
    public void clearAllEntries() {
        entries.clear();
    }

    // Get most recent entries
    public List<DiaryEntry> getRecentEntries(int limit) {
        return entries.values().stream()
                .sorted(Comparator.comparing(DiaryEntry::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Set all entries (used for loading from file)
    public void setEntries(Map<LocalDate, DiaryEntry> entries) {
        if (entries != null) {
            this.entries = new HashMap<>(entries);
        }
    }
}