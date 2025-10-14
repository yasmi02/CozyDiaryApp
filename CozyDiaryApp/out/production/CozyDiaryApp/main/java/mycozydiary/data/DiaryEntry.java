package mycozydiary.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

public class DiaryEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private LocalDateTime timestamp;
    private String mood;
    private String[] photos;

    public DiaryEntry(String title, String content, LocalDateTime timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.mood = "😊";
        this.photos = new String[0];
    }

    public DiaryEntry(String title, String content, LocalDateTime timestamp, String mood) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.mood = mood;
        this.photos = new String[0];
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMood() { return mood; }
    public String[] getPhotos() { return photos; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setMood(String mood) { this.mood = mood; }
    public void setPhotos(String[] photos) { this.photos = photos; }

    public String getFormattedDate() {
        return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}