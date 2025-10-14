// ==================== FILE: src/main/java/mycozydiary/ui/screens/HomeScreen.java ====================
package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.DataManager;
import mycozydiary.ui.components.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeScreen extends JPanel {
    private DataManager dataManager;

    public HomeScreen(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.LIGHT_LILAC);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("My Day ☀");
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);

        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        dateLabel.setFont(Theme.BODY_FONT);
        dateLabel.setForeground(Theme.TEXT_DARK);

        JPanel headerTextPanel = new JPanel(new BorderLayout());
        headerTextPanel.setBackground(Theme.LIGHT_LILAC);
        headerTextPanel.add(titleLabel, BorderLayout.NORTH);
        headerTextPanel.add(dateLabel, BorderLayout.SOUTH);

        header.add(headerTextPanel, BorderLayout.CENTER);

        return header;
    }

    private JScrollPane createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_LILAC);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        CardPanel quoteCard = new CardPanel("💗",
                "Believe in yourself\nand you will be unstoppable.",
                "Goal of gratitude");

        JPanel habitCard = createHabitTrackerCard();
        JPanel bottomButtons = createBottomButtons();

        content.add(quoteCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(habitCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(bottomButtons);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBackground(Theme.BG_LILAC);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createHabitTrackerCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Theme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel("Habit Tracker");
        title.setFont(Theme.HEADING_FONT);
        title.setForeground(Theme.TEXT_DARK);

        JPanel habitsPanel = new JPanel();
        habitsPanel.setLayout(new BoxLayout(habitsPanel, BoxLayout.Y_AXIS));
        habitsPanel.setBackground(Theme.WHITE);

        String[] habitIcons = {"🌸", "📖", "💧", "🧘"};
        String[] habitNames = {"Skincare", "Reading", "Hydration", "Meditation"};
        Color[] habitColors = {Theme.ACCENT_PURPLE, new Color(173, 216, 230),
                Theme.LILAC, new Color(221, 160, 221)};

        for (int i = 0; i < habitNames.length; i++) {
            HabitRow habitRow = new HabitRow(habitIcons[i], habitNames[i], habitColors[i], dataManager);
            habitsPanel.add(habitRow);
            if (i < habitNames.length - 1) {
                habitsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        card.add(title, BorderLayout.NORTH);
        card.add(habitsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBottomButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Theme.BG_LILAC);
        panel.setMaximumSize(new Dimension(400, 100));

        JButton saveBtn = new JButton("💾 Save Habits");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        saveBtn.setBackground(Theme.ACCENT_PURPLE);
        saveBtn.setForeground(Theme.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveBtn.addActionListener(e -> {
            // The habits are already auto-saved when you check them,
            // but this gives user feedback
            JOptionPane.showMessageDialog(
                    this,
                    "All your habits have been saved! ✨\n\nKeep up the great work! 💜",
                    "Habits Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(Theme.DEEP_LILAC);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(Theme.ACCENT_PURPLE);
            }
        });

        panel.add(saveBtn);

        return panel;
    }

    private JButton createActionButton(String emoji, String text) {
        JButton btn = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        btn.setFont(Theme.SMALL_FONT);
        btn.setBackground(Theme.WHITE);
        btn.setForeground(Theme.TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}