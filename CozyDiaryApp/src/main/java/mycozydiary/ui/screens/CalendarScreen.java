package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.*;
import mycozydiary.ui.Utils;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CalendarScreen extends JPanel {
    private DataManager dataManager;
    private Runnable switchToJournalCallback;

    public CalendarScreen(DataManager dataManager, Runnable backAction, Runnable switchToJournalCallback) {
        this.dataManager = dataManager;
        this.switchToJournalCallback = switchToJournalCallback;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createHeader(backAction), BorderLayout.NORTH);
        add(createCalendar(), BorderLayout.CENTER);
    }

    private JPanel createHeader(Runnable backAction) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.LIGHT_LILAC);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.setBackground(Theme.LIGHT_LILAC);
        backBtn.setForeground(Theme.TEXT_DARK);
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> backAction.run());

        JLabel titleLabel = new JLabel("Calendar 📅");
        titleLabel.setFont(Theme.SUBTITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);

        return header;
    }

    private JScrollPane createCalendar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());

        JLabel monthLabel = new JLabel(yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        monthLabel.setFont(Theme.HEADING_FONT);
        monthLabel.setForeground(Theme.TEXT_DARK);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel calGrid = new JPanel(new GridLayout(7, 7, 5, 5));
        calGrid.setBackground(Theme.BG_LILAC);

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(Theme.TEXT_DARK);
            calGrid.add(label);
        }

        LocalDate firstDay = yearMonth.atDay(1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < startDayOfWeek; i++) {
            calGrid.add(new JLabel(""));
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            JPanel dayPanel = createDayPanel(date, today);
            calGrid.add(dayPanel);
        }

        panel.add(monthLabel, BorderLayout.NORTH);
        panel.add(calGrid, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBackground(Theme.BG_LILAC);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    private JPanel createDayPanel(LocalDate date, LocalDate today) {
        boolean hasEntry = dataManager.hasEntry(date);
        boolean isToday = date.equals(today);

        JPanel dayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw circle indicator for journal entries
                if (hasEntry) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw a small circle at the bottom center
                    int circleSize = 8;
                    int x = (getWidth() - circleSize) / 2;
                    int y = getHeight() - circleSize - 5;

                    g2.setColor(Theme.ACCENT_PURPLE);
                    g2.fill(new Ellipse2D.Float(x, y, circleSize, circleSize));

                    g2.dispose();
                }
            }
        };

        dayPanel.setLayout(new BorderLayout());
        dayPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set background color
        if (isToday) {
            dayPanel.setBackground(Theme.CALENDAR_TODAY);
        } else {
            dayPanel.setBackground(Theme.CALENDAR_DAY);
        }

        dayPanel.setBorder(new LineBorder(Theme.LILAC, 1));

        // Day number label
        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dayLabel.setFont(Theme.BODY_FONT);
        dayLabel.setForeground(isToday ? Theme.WHITE : Theme.TEXT_DARK);

        dayPanel.add(dayLabel, BorderLayout.CENTER);

        // Click handler
        dayPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showEntryForDate(date);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isToday) {
                    dayPanel.setBackground(Theme.DARKER_PURPLE);
                } else {
                    dayPanel.setBackground(Theme.LIGHT_LILAC);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (isToday) {
                    dayPanel.setBackground(Theme.CALENDAR_TODAY);
                } else {
                    dayPanel.setBackground(Theme.CALENDAR_DAY);
                }
            }
        });

        return dayPanel;
    }

    private void showEntryForDate(LocalDate date) {
        DiaryEntry entry = dataManager.getEntry(date);
        if (entry != null) {
            String message = "📅 " + entry.getFormattedDate() + " - " + entry.getFormattedTime() + "\n" +
                    "Mood: " + entry.getMood() + "\n\n" +
                    "Title: " + entry.getTitle() + "\n\n" +
                    entry.getContent();

            Utils.showInfoDialog(this, message, "Entry for " + date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        } else {
            int result = Utils.showConfirmDialog(this,
                    "No entry for this date. Create one?",
                    "No Entry");
            if (result == JOptionPane.YES_OPTION && switchToJournalCallback != null) {
                switchToJournalCallback.run();
            }
        }
    }
}